(ns graintime.date-parser
  "Parse Unix date command formats into Java ZonedDateTime
  
  Supports formats from `date` command:
  - date                          => Tue Oct 22 21:30:45 PDT 2025
  - date -Iseconds                => 2025-10-22T21:30:45-07:00
  - date --rfc-3339=seconds       => 2025-10-22 21:30:45-07:00
  - date '+%Y-%m-%d %H:%M:%S %z'  => 2025-10-22 21:30:45 -0700
  - Custom format                 => 2025-10-22 21:30"
  (:import [java.time ZonedDateTime LocalDateTime ZoneId]
           [java.time.format DateTimeFormatter DateTimeFormatterBuilder]
           [java.time.temporal ChronoField]))

(def common-date-formatters
  "Common date format patterns that Unix date command produces"
  [;; ISO 8601 with timezone: 2025-10-22T21:30:45-07:00
   (DateTimeFormatter/ISO_OFFSET_DATE_TIME)
   
   ;; RFC 3339: 2025-10-22 21:30:45-07:00
   (DateTimeFormatter/ofPattern "yyyy-MM-dd HH:mm:ssXXX")
   
   ;; With timezone offset: 2025-10-22 21:30:45 -0700
   (DateTimeFormatter/ofPattern "yyyy-MM-dd HH:mm:ss Z")
   
   ;; Simple date time: 2025-10-22 21:30:45
   (DateTimeFormatter/ofPattern "yyyy-MM-dd HH:mm:ss")
   
   ;; Simple date time short: 2025-10-22 21:30
   (DateTimeFormatter/ofPattern "yyyy-MM-dd HH:mm")
   
   ;; Unix date default 24-hour: Tue Oct 22 21:30:45 PDT 2025
   (DateTimeFormatter/ofPattern "EEE MMM dd HH:mm:ss z yyyy")
   
   ;; Unix date 12-hour with AM/PM: Wed Oct 22 09:29:05 PM PDT 2025
   (DateTimeFormatter/ofPattern "EEE MMM dd hh:mm:ss a z yyyy")
   
   ;; Date only: 2025-10-22
   (DateTimeFormatter/ofPattern "yyyy-MM-dd")])

(defn parse-unix-date
  "Parse a date string in various Unix date command formats
  
  Returns ZonedDateTime in Pacific timezone if no timezone specified
  
  Examples:
    (parse-unix-date \"2025-10-22T21:30:45-07:00\")
    (parse-unix-date \"2025-10-22 21:30:45-07:00\")
    (parse-unix-date \"2025-10-22 21:30:45\")
    (parse-unix-date \"2025-10-22 21:30\")
    (parse-unix-date \"Tue Oct 22 21:30:45 PDT 2025\")"
  [date-str]
  (let [zone-id (ZoneId/of "America/Los_Angeles")  ; Default to Pacific
        trimmed (clojure.string/trim date-str)]
    
    ;; Try each formatter
    (or
      (some
        (fn [formatter]
          (try
            ;; Try parsing with timezone info
            (ZonedDateTime/parse trimmed formatter)
            (catch Exception _
              (try
                ;; Try parsing as LocalDateTime and add Pacific timezone
                (let [local-dt (LocalDateTime/parse trimmed formatter)]
                  (ZonedDateTime/of local-dt zone-id))
                (catch Exception _ nil)))))
        common-date-formatters)
      
      ;; If all parsers fail, throw informative error
      (throw (ex-info (str "Cannot parse date string: " date-str)
                      {:date-str date-str
                       :supported-formats
                       ["2025-10-22T21:30:45-07:00"
                        "2025-10-22 21:30:45-07:00"
                        "2025-10-22 21:30:45"
                        "2025-10-22 21:30"
                        "2025-10-22"
                        "Tue Oct 22 21:30:45 PDT 2025"]})))))

(defn parse-date-with-examples
  "Parse date with helpful error messages if it fails"
  [date-str]
  (try
    (parse-unix-date date-str)
    (catch Exception e
      (println "❌ Error parsing date:" (.getMessage e))
      (println "")
      (println "📅 Supported date formats:")
      (println "  • ISO 8601:           2025-10-22T21:30:45-07:00")
      (println "  • RFC 3339:           2025-10-22 21:30:45-07:00")
      (println "  • Simple:             2025-10-22 21:30:45")
      (println "  • Simple short:       2025-10-22 21:30")
      (println "  • Date only:          2025-10-22")
      (println "  • Unix date:          Tue Oct 22 21:30:45 PDT 2025")
      (println "")
      (println "💡 TIP: Use output from `date` command:")
      (println "  date                      # Default format")
      (println "  date -Iseconds            # ISO 8601")
      (println "  date --rfc-3339=seconds   # RFC 3339")
      (System/exit 1))))

;; Test function
(defn test-date-parser
  "Test the date parser with various formats"
  []
  (let [test-dates
        ["2025-10-22T21:30:45-07:00"
         "2025-10-22 21:30:45-07:00"
         "2025-10-22 21:30:45"
         "2025-10-22 21:30"
         "2025-10-22"
         "Tue Oct 22 21:30:45 PDT 2025"]]
    
    (println "🧪 Testing date parser...")
    (doseq [date-str test-dates]
      (try
        (let [parsed (parse-unix-date date-str)]
          (println (format "✅ \"%s\" => %s" date-str parsed)))
        (catch Exception e
          (println (format "❌ \"%s\" => %s" date-str (.getMessage e))))))))

