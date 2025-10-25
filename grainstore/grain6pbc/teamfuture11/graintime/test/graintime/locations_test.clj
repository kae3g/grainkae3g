(ns graintime.locations-test
  "Test graintime generation for multiple global locations"
  (:require [clojure.test :refer [deftest is testing]]
            [graintime.locations :as loc]))

(deftest test-location-normalization
  (testing "Normalize location keywords"
    (is (= :kyoto (loc/normalize-location :kyoto)))
    (is (= :barcelona (loc/normalize-location :barcelona)))
    (is (= :london (loc/normalize-location :london))))
  
  (testing "Normalize location strings"
    (is (= :kyoto (loc/normalize-location "kyoto")))
    (is (= :barcelona (loc/normalize-location "barcelona")))
    (is (= :london (loc/normalize-location "london"))))
  
  (testing "Normalize location aliases"
    (is (= :kyoto (loc/normalize-location "jp")))
    (is (= :kyoto (loc/normalize-location "japan")))
    (is (= :barcelona (loc/normalize-location "bcn")))
    (is (= :barcelona (loc/normalize-location "spain")))
    (is (= :barcelona (loc/normalize-location "es")))
    (is (= :london (loc/normalize-location "uk")))
    (is (= :london (loc/normalize-location "gb")))))

(deftest test-get-location
  (testing "Get location data by keyword"
    (let [kyoto (loc/get-location :kyoto)]
      (is (= "Kyoto" (:city kyoto)))
      (is (= "Japan" (:country kyoto)))
      (is (= "Asia/Tokyo" (:tz kyoto)))
      (is (= 35.0116 (:lat kyoto)))
      (is (= 135.7681 (:lon kyoto)))))
  
  (testing "Get location data by string"
    (let [barcelona (loc/get-location "barcelona")]
      (is (= "Barcelona" (:city barcelona)))
      (is (= "Spain" (:country barcelona)))
      (is (= "Europe/Madrid" (:tz barcelona)))))
  
  (testing "Get location data by alias"
    (let [london (loc/get-location "uk")]
      (is (= "London" (:city london)))
      (is (= "UK" (:country london)))
      (is (= "Europe/London" (:tz london))))))

(deftest test-location-display-names
  (testing "Display names for all locations"
    (is (= "Kyoto, Japan" (loc/location-display-name :kyoto)))
    (is (= "Barcelona, Spain" (loc/location-display-name :barcelona)))
    (is (= "London, UK" (loc/location-display-name :london)))
    (is (= "San Rafael, California, USA" (loc/location-display-name :san-rafael)))
    (is (= "Caspar, California, USA" (loc/location-display-name :caspar)))))

(deftest test-all-locations-exist
  (testing "All expected locations are defined"
    (is (contains? loc/locations :san-rafael))
    (is (contains? loc/locations :kyoto))
    (is (contains? loc/locations :barcelona))
    (is (contains? loc/locations :london))
    (is (contains? loc/locations :caspar))))

(deftest test-timezone-data
  (testing "All locations have valid timezone data"
    (doseq [[k v] loc/locations]
      (is (string? (:tz v)) (str "Location " k " has timezone"))
      (is (re-matches #"^[A-Z][a-z]+/[A-Za-z_]+$" (:tz v))
          (str "Location " k " has valid timezone format")))))

(comment
  ;; Run all tests
  (clojure.test/run-tests 'graintime.locations-test)
  
  ;; Test specific function
  (loc/get-location "kyoto")
  (loc/get-location "bcn")
  (loc/location-display-name :london))

