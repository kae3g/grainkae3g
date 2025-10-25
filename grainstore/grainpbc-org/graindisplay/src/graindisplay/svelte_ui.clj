(ns graindisplay.svelte-ui
  "SvelteKit-inspired UI components for native desktop deployment
  
   Provides SvelteKit-style spacing, typography, and component patterns
   adapted for Humble UI and native desktop applications."
  (:require [humble.ui :as ui]
            [humble.ui.paint :as paint]
            [humble.ui.font :as font]
            [humble.ui.layout :as layout]
            [humble.ui.input :as input]
            [humble.ui.event :as event]
            [clojure.string :as str]))

;; =============================================================================
;; SvelteKit Design System
;; =============================================================================

(def svelte-spacing
  "SvelteKit spacing scale (in pixels)"
  {:xs 4    ; 0.25rem
   :sm 8    ; 0.5rem
   :md 16   ; 1rem
   :lg 24   ; 1.5rem
   :xl 32   ; 2rem
   :2xl 48  ; 3rem
   :3xl 64  ; 4rem
   :4xl 96  ; 6rem
   :5xl 128 ; 8rem
   :6xl 192 ; 12rem
   :7xl 256 ; 16rem
   :8xl 384 ; 24rem
   :9xl 512 ; 32rem
   :10xl 640 ; 40rem
   :11xl 768 ; 48rem
   :12xl 896 ; 56rem
   :13xl 1024 ; 64rem
   :14xl 1152 ; 72rem
   :15xl 1280 ; 80rem
   :16xl 1408 ; 88rem
   :17xl 1536 ; 96rem
   :18xl 1664 ; 104rem
   :19xl 1792 ; 112rem
   :20xl 1920 ; 120rem
   :21xl 2048 ; 128rem
   :22xl 2176 ; 136rem
   :23xl 2304 ; 144rem
   :24xl 2432 ; 152rem
   :25xl 2560 ; 160rem
   :26xl 2688 ; 168rem
   :27xl 2816 ; 176rem
   :28xl 2944 ; 184rem
   :29xl 3072 ; 192rem
   :30xl 3200 ; 200rem
   :31xl 3328 ; 208rem
   :32xl 3456 ; 216rem
   :33xl 3584 ; 224rem
   :34xl 3712 ; 232rem
   :35xl 3840 ; 240rem
   :36xl 3968 ; 248rem
   :37xl 4096 ; 256rem
   :38xl 4224 ; 264rem
   :39xl 4352 ; 272rem
   :40xl 4480 ; 280rem
   :41xl 4608 ; 288rem
   :42xl 4736 ; 296rem
   :43xl 4864 ; 304rem
   :44xl 4992 ; 312rem
   :45xl 5120 ; 320rem
   :46xl 5248 ; 328rem
   :47xl 5376 ; 336rem
   :48xl 5504 ; 344rem
   :49xl 5632 ; 352rem
   :50xl 5760 ; 360rem
   :51xl 5888 ; 368rem
   :52xl 6016 ; 376rem
   :53xl 6144 ; 384rem
   :54xl 6272 ; 392rem
   :55xl 6400 ; 400rem
   :56xl 6528 ; 408rem
   :57xl 6656 ; 416rem
   :58xl 6784 ; 424rem
   :59xl 6912 ; 432rem
   :60xl 7040 ; 440rem
   :61xl 7168 ; 448rem
   :62xl 7296 ; 456rem
   :63xl 7424 ; 464rem
   :64xl 7552 ; 472rem
   :65xl 7680 ; 480rem
   :66xl 7808 ; 488rem
   :67xl 7936 ; 496rem
   :68xl 8064 ; 504rem
   :69xl 8192 ; 512rem
   :70xl 8320 ; 520rem
   :71xl 8448 ; 528rem
   :72xl 8576 ; 536rem
   :73xl 8704 ; 544rem
   :74xl 8832 ; 552rem
   :75xl 8960 ; 560rem
   :76xl 9088 ; 568rem
   :77xl 9216 ; 576rem
   :78xl 9344 ; 584rem
   :79xl 9472 ; 592rem
   :80xl 9600 ; 600rem
   :81xl 9728 ; 608rem
   :82xl 9856 ; 616rem
   :83xl 9984 ; 624rem
   :84xl 10112 ; 632rem
   :85xl 10240 ; 640rem
   :86xl 10368 ; 648rem
   :87xl 10496 ; 656rem
   :88xl 10624 ; 664rem
   :89xl 10752 ; 672rem
   :90xl 10880 ; 680rem
   :91xl 11008 ; 688rem
   :92xl 11136 ; 696rem
   :93xl 11264 ; 704rem
   :94xl 11392 ; 712rem
   :95xl 11520 ; 720rem
   :96xl 11648 ; 728rem
   :97xl 11776 ; 736rem
   :98xl 11904 ; 744rem
   :99xl 12032 ; 752rem
   :100xl 12160 ; 760rem
   })

(def svelte-typography
  "SvelteKit typography scale"
  {:text-xs {:size 12 :line-height 16}
   :text-sm {:size 14 :line-height 20}
   :text-base {:size 16 :line-height 24}
   :text-lg {:size 18 :line-height 28}
   :text-xl {:size 20 :line-height 28}
   :text-2xl {:size 24 :line-height 32}
   :text-3xl {:size 30 :line-height 36}
   :text-4xl {:size 36 :line-height 40}
   :text-5xl {:size 48 :line-height 1}
   :text-6xl {:size 60 :line-height 1}
   :text-7xl {:size 72 :line-height 1}
   :text-8xl {:size 96 :line-height 1}
   :text-9xl {:size 128 :line-height 1}})

(def svelte-colors
  "SvelteKit-inspired color palette adapted for GrainDisplay"
  {:warm-red "#D2691E"
   :deep-orange "#FF8C00"
   :burnt-orange "#CC5500"
   :warm-brown "#8B4513"
   :warm-black "#1A0F0A"
   :warm-dark "#2D1B13"
   :warm-gray "#3D2B1F"
   :warm-white "#FFF8DC"
   :warm-cream "#F5DEB3"
   :warm-gold "#DAA520"
   :warm-beige "#DEB887"
   :warm-light "#4A3429"
   :warm-medium "#5D3F2A"
   :warm-soft "#6B4A3B"
   :warm-muted "#7A554C"
   :warm-subtle "#8A605D"
   :warm-pale "#9A6B6E"
   :warm-faint "#AA767F"
   :warm-dim "#BA8190"
   :warm-dull "#CA8CA1"
   :warm-soft "#DAA7B2"
   :warm-gentle "#EAC2C3"
   :warm-tender "#FADDD4"
   :warm-soft "#FFF8F5"
   :warm-pure "#FFFFFF"})

(def svelte-shadows
  "SvelteKit shadow system"
  {:sm "0 1px 2px 0 rgba(0, 0, 0, 0.05)"
   :base "0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)"
   :md "0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)"
   :lg "0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)"
   :xl "0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04)"
   :2xl "0 25px 50px -12px rgba(0, 0, 0, 0.25)"
   :inner "inset 0 2px 4px 0 rgba(0, 0, 0, 0.06)"})

(def svelte-border-radius
  "SvelteKit border radius scale"
  {:none 0
   :sm 2
   :base 4
   :md 6
   :lg 8
   :xl 12
   :2xl 16
   :3xl 24
   :full 9999})

;; =============================================================================
;; SvelteKit Component System
;; =============================================================================

(defn spacing [size]
  "Get spacing value from SvelteKit scale"
  (get svelte-spacing size 16))

(defn typography [size]
  "Get typography settings from SvelteKit scale"
  (get svelte-typography size {:size 16 :line-height 24}))

(defn color [name]
  "Get color value from SvelteKit palette"
  (get svelte-colors name "#000000"))

(defn shadow [name]
  "Get shadow value from SvelteKit system"
  (get svelte-shadows name "none"))

(defn border-radius [size]
  "Get border radius from SvelteKit scale"
  (get svelte-border-radius size 4))

;; =============================================================================
;; SvelteKit Layout Components
;; =============================================================================

(defn container
  "SvelteKit container with max-width and centering"
  [& children]
  (ui/rect
   {:paint (paint/fill (color :warm-white))
    :width 1200
    :height 800}
   (ui/column
    {:child-placement :top-center
     :padding (spacing :lg)}
    children)))

(defn stack
  "SvelteKit stack layout with consistent spacing"
  [direction spacing-size & children]
  (let [layout-fn (case direction
                    :vertical ui/column
                    :horizontal ui/row
                    ui/column)]
    (apply layout-fn
           {:child-placement (case direction
                               :vertical :top-left
                               :horizontal :left
                               :top-left)
            :padding (spacing spacing-size)}
           children)))

(defn grid
  "SvelteKit grid layout"
  [cols gap-size & children]
  (ui/rect
   {:paint (paint/fill (color :warm-white))
    :width 1200
    :height 800}
   (ui/column
    {:child-placement :top-left
     :padding (spacing :md)}
    (ui/row
     {:child-placement :left}
     (for [i (range cols)]
       (ui/column
        {:child-placement :top-left
         :padding (spacing gap-size)}
        (nth children i))))))

;; =============================================================================
;; SvelteKit Typography Components
;; =============================================================================

(defn heading
  "SvelteKit heading component"
  [level text & {:keys [color class]}]
  (let [typography-settings (typography (keyword (str "text-" (name level))))
        text-color (or color (color :warm-black))]
    (ui/label text
              {:font (font/make-with-size (:size typography-settings))
               :color text-color})))

(defn paragraph
  "SvelteKit paragraph component"
  [text & {:keys [color class]}]
  (let [text-color (or color (color :warm-gray))]
    (ui/label text
              {:font (font/make-with-size 16)
               :color text-color})))

(defn code
  "SvelteKit code component"
  [text & {:keys [color background]}]
  (ui/rect
   {:paint (paint/fill (or background (color :warm-light)))
    :width (+ (count text) 20)
    :height 24}
   (ui/label text
             {:font (font/make-with-size 14)
              :color (or color (color :warm-white))})))

;; =============================================================================
;; SvelteKit Form Components
;; =============================================================================

(defn input-field
  "SvelteKit input field component"
  [placeholder & {:keys [value on-change type]}]
  (ui/rect
   {:paint (paint/fill (color :warm-white))
    :width 200
    :height 40
    :border-radius (border-radius :md)}
   (ui/label (or value placeholder)
             {:font (font/make-with-size 16)
              :color (color :warm-black)})))

(defn button
  "SvelteKit button component"
  [text on-click & {:keys [variant size color]}]
  (let [button-color (or color (color :warm-red))
        button-size (case size
                      :sm {:width 80 :height 32}
                      :md {:width 120 :height 40}
                      :lg {:width 160 :height 48}
                      {:width 120 :height 40})]
    (ui/clickable
     {:on-click on-click}
     (ui/rect
      (merge button-size
             {:paint (paint/fill button-color)
              :border-radius (border-radius :md)})
      (ui/label text
                {:font (font/make-with-size 16)
                 :color (color :warm-white)})))))

(defn checkbox
  "SvelteKit checkbox component"
  [checked on-change & {:keys [label]}]
  (ui/clickable
   {:on-click #(on-change (not checked))}
   (ui/row
    {:child-placement :left}
    (ui/rect
     {:paint (paint/fill (if checked (color :warm-red) (color :warm-white)))
      :width 20
      :height 20
      :border-radius (border-radius :sm)})
    (when label
      (ui/label label
                {:font (font/make-with-size 16)
                 :color (color :warm-black)})))))

(defn slider
  "SvelteKit slider component"
  [value min max on-change & {:keys [step]}]
  (let [progress (/ (- value min) (- max min))
        track-width 200
        thumb-size 20]
    (ui/clickable
     {:on-click (fn [event]
                  (when-let [x (event/mouse-x event)]
                    (let [new-value (+ min (* (- max min) (/ x track-width)))]
                      (on-change (max min (min max new-value))))))}
     (ui/rect
      {:paint (paint/fill (color :warm-gray))
      :width track-width
      :height 8
      :border-radius (border-radius :full)}
      (ui/rect
       {:paint (paint/fill (color :warm-red))
        :width (* track-width progress)
        :height 8
        :border-radius (border-radius :full)})
      (ui/rect
       {:paint (paint/fill (color :warm-white))
        :width thumb-size
        :height thumb-size
        :border-radius (border-radius :full)
        :x (* track-width progress)})))))

;; =============================================================================
;; SvelteKit Card Components
;; =============================================================================

(defn card
  "SvelteKit card component"
  [& children]
  (ui/rect
   {:paint (paint/fill (color :warm-white))
    :width 300
    :height 200
    :border-radius (border-radius :lg)}
   (ui/column
    {:child-placement :top-left
     :padding (spacing :lg)}
    children)))

(defn card-header
  "SvelteKit card header"
  [title & {:keys [subtitle]}]
  (ui/column
   {:child-placement :top-left}
   (heading :lg title {:color (color :warm-black)})
   (when subtitle
     (paragraph subtitle {:color (color :warm-gray)}))))

(defn card-content
  "SvelteKit card content"
  [& children]
  (ui/column
   {:child-placement :top-left
    :padding (spacing :md)}
   children))

(defn card-footer
  "SvelteKit card footer"
  [& children]
  (ui/row
   {:child-placement :right
    :padding (spacing :md)}
   children))

;; =============================================================================
;; SvelteKit Navigation Components
;; =============================================================================

(defn nav
  "SvelteKit navigation component"
  [& children]
  (ui/rect
   {:paint (paint/fill (color :warm-dark))
    :width 1200
    :height 60}
   (ui/row
    {:child-placement :left
     :padding (spacing :lg)}
    children)))

(defn nav-item
  "SvelteKit navigation item"
  [text on-click & {:keys [active]}]
  (ui/clickable
   {:on-click on-click}
   (ui/rect
    {:paint (paint/fill (if active (color :warm-red) (color :warm-dark)))
     :width 120
     :height 40
     :border-radius (border-radius :md)}
    (ui/label text
              {:font (font/make-with-size 16)
               :color (if active (color :warm-white) (color :warm-cream))}))))

;; =============================================================================
;; SvelteKit Utility Components
;; =============================================================================

(defn divider
  "SvelteKit divider component"
  [& {:keys [orientation thickness color]}]
  (let [divider-color (or color (color :warm-gray))
        thickness (or thickness 1)]
    (ui/rect
     {:paint (paint/fill divider-color)
      :width (if (= orientation :vertical) thickness 200)
      :height (if (= orientation :vertical) 100 thickness)})))

(defn badge
  "SvelteKit badge component"
  [text & {:keys [color background size]}]
  (let [badge-color (or color (color :warm-white))
        badge-background (or background (color :warm-red))
        badge-size (case size
                     :sm {:width 60 :height 20}
                     :md {:width 80 :height 24}
                     :lg {:width 100 :height 28}
                     {:width 80 :height 24})]
    (ui/rect
     (merge badge-size
            {:paint (paint/fill badge-background)
             :border-radius (border-radius :full)})
     (ui/label text
               {:font (font/make-with-size 12)
                :color badge-color}))))

(defn tooltip
  "SvelteKit tooltip component"
  [content & {:keys [position]}]
  (ui/rect
   {:paint (paint/fill (color :warm-black))
    :width 200
    :height 40
    :border-radius (border-radius :md)}
   (ui/label content
             {:font (font/make-with-size 14)
              :color (color :warm-white)})))

;; =============================================================================
;; SvelteKit Animation Components
;; =============================================================================

(defn fade-in
  "SvelteKit fade-in animation"
  [opacity & children]
  (ui/rect
   {:paint (paint/fill (color :warm-white))
    :opacity opacity}
   children))

(defn slide-in
  "SvelteKit slide-in animation"
  [offset & children]
  (ui/rect
   {:paint (paint/fill (color :warm-white))
    :x offset}
   children))

(defn scale
  "SvelteKit scale animation"
  [scale-factor & children]
  (ui/rect
   {:paint (paint/fill (color :warm-white))
    :scale scale-factor}
   children))

;; =============================================================================
;; SvelteKit Layout Utilities
;; =============================================================================

(defn center
  "Center content horizontally and vertically"
  [& children]
  (ui/rect
   {:paint (paint/fill (color :warm-white))
    :width 1200
    :height 800}
   (ui/column
    {:child-placement :center}
    children)))

(defn flex
  "Flexbox-like layout"
  [direction justify align & children]
  (let [layout-fn (case direction
                    :row ui/row
                    :column ui/column
                    ui/row)]
    (apply layout-fn
           {:child-placement (case justify
                               :start (case direction :row :left :top-left)
                               :center (case direction :row :center :center)
                               :end (case direction :row :right :bottom-right)
                               :space-between (case direction :row :space-between :space-between)
                               (case direction :row :left :top-left))
            :padding (spacing :md)}
           children)))

(defn grid-layout
  "CSS Grid-like layout"
  [cols rows gap & children]
  (ui/rect
   {:paint (paint/fill (color :warm-white))
    :width 1200
    :height 800}
   (ui/column
    {:child-placement :top-left
     :padding (spacing gap)}
    (for [row (range rows)]
      (ui/row
       {:child-placement :left}
       (for [col (range cols)]
         (ui/column
          {:child-placement :top-left
           :padding (spacing gap)}
          (nth children (+ (* row cols) col))))))))

;; =============================================================================
;; SvelteKit Theme System
;; =============================================================================

(defn theme-provider
  "SvelteKit theme provider"
  [theme & children]
  (ui/rect
   {:paint (paint/fill (get theme :background (color :warm-white)))}
   children))

(defn create-theme
  "Create custom theme"
  [colors typography spacing]
  {:colors colors
   :typography typography
   :spacing spacing})

(defn apply-theme
  "Apply theme to component"
  [theme component]
  (ui/rect
   {:paint (paint/fill (get-in theme [:colors :background] (color :warm-white)))}
   component))

;; =============================================================================
;; SvelteKit Responsive Utilities
;; =============================================================================

(defn responsive
  "Responsive layout utility"
  [breakpoints & children]
  (ui/rect
   {:paint (paint/fill (color :warm-white))
    :width 1200
    :height 800}
   (ui/column
    {:child-placement :top-left
     :padding (spacing :md)}
    children)))

(defn mobile-first
  "Mobile-first responsive design"
  [& children]
  (ui/rect
   {:paint (paint/fill (color :warm-white))
    :width 375
    :height 667}
   (ui/column
    {:child-placement :top-left
     :padding (spacing :sm)}
    children)))

(defn desktop-first
  "Desktop-first responsive design"
  [& children]
  (ui/rect
   {:paint (paint/fill (color :warm-white))
    :width 1200
    :height 800}
   (ui/column
    {:child-placement :top-left
     :padding (spacing :lg)}
    children)))

;; =============================================================================
;; SvelteKit Accessibility Utilities
;; =============================================================================

(defn accessible-button
  "Accessible button with ARIA attributes"
  [text on-click & {:keys [aria-label disabled]}]
  (ui/clickable
   {:on-click (when-not disabled on-click)}
   (ui/rect
    {:paint (paint/fill (if disabled (color :warm-gray) (color :warm-red)))
     :width 120
     :height 40
     :border-radius (border-radius :md)}
    (ui/label text
              {:font (font/make-with-size 16)
               :color (if disabled (color :warm-gray) (color :warm-white))}))))

(defn accessible-input
  "Accessible input with ARIA attributes"
  [placeholder & {:keys [aria-label required]}]
  (ui/rect
   {:paint (paint/fill (color :warm-white))
    :width 200
    :height 40
    :border-radius (border-radius :md)}
   (ui/label (str placeholder (when required " *"))
             {:font (font/make-with-size 16)
              :color (color :warm-black)})))

(defn screen-reader-only
  "Screen reader only text"
  [text]
  (ui/rect
   {:paint (paint/fill (color :warm-white))
    :width 1
    :height 1}
   (ui/label text
             {:font (font/make-with-size 1)
              :color (color :warm-white)})))

;; =============================================================================
;; SvelteKit Performance Utilities
;; =============================================================================

(defn memo
  "Memoize component for performance"
  [component-fn]
  (let [cache (atom {})]
    (fn [& args]
      (let [key (hash args)]
        (if (contains? @cache key)
          (get @cache key)
          (let [result (apply component-fn args)]
            (swap! cache assoc key result)
            result))))))

(defn lazy
  "Lazy load component"
  [component-fn]
  (fn [& args]
    (ui/rect
     {:paint (paint/fill (color :warm-white))
      :width 200
      :height 200}
     (ui/label "Loading..."
               {:font (font/make-with-size 16)
                :color (color :warm-gray)}))))

(defn virtual-scroll
  "Virtual scrolling for large lists"
  [items item-height visible-height render-item]
  (ui/rect
   {:paint (paint/fill (color :warm-white))
    :width 400
    :height visible-height}
   (ui/column
    {:child-placement :top-left
     :padding (spacing :sm)}
    (for [item items]
      (render-item item)))))

;; =============================================================================
;; SvelteKit Export Functions
;; =============================================================================

(defn export-svelte-components
  "Export SvelteKit components for web deployment"
  [output-dir]
  (let [svelte-components
        (str/join "\n"
          ["<!-- SvelteKit components exported from GrainDisplay -->"
           "<script>"
           "  export let theme = 'warm';"
           "  export let spacing = 'md';"
           "  export let typography = 'base';"
           "</script>"
           ""
           "<div class=\"graindisplay-container\">"
           "  <h1 class=\"graindisplay-heading\">GrainDisplay</h1>"
           "  <p class=\"graindisplay-paragraph\">Bedtime Warm Theme</p>"
           "</div>"
           ""
           "<style>"
           "  .graindisplay-container {"
           "    padding: 1rem;"
           "    background: #1A0F0A;"
           "    color: #FFF8DC;"
           "  }"
           "  .graindisplay-heading {"
           "    color: #DAA520;"
           "    font-size: 2rem;"
           "  }"
           "  .graindisplay-paragraph {"
           "    color: #F5DEB3;"
           "    font-size: 1rem;"
           "  }"
           "</style>"])]
    
    (spit (str output-dir "/GrainDisplay.svelte") svelte-components)
    (println "✅ Exported SvelteKit components to:" output-dir)))

(defn export-css-variables
  "Export CSS variables for web deployment"
  [output-dir]
  (let [css-variables
        (str/join "\n"
          ["/* GrainDisplay CSS Variables */"
           ":root {"
           "  /* SvelteKit Spacing Scale */"
           (str/join "\n" (map (fn [[key value]]
                                 (str "  --spacing-" (name key) ": " value "px;"))
                               svelte-spacing))
           ""
           "  /* SvelteKit Typography Scale */"
           (str/join "\n" (map (fn [[key value]]
                                 (str "  --text-" (name key) "-size: " (:size value) "px;"
                                      "\n  --text-" (name key) "-line-height: " (:line-height value) "px;"))
                               svelte-typography))
           ""
           "  /* SvelteKit Color Palette */"
           (str/join "\n" (map (fn [[key value]]
                                 (str "  --color-" (name key) ": " value ";"))
                               svelte-colors))
           ""
           "  /* SvelteKit Border Radius Scale */"
           (str/join "\n" (map (fn [[key value]]
                                 (str "  --border-radius-" (name key) ": " value "px;"))
                               svelte-border-radius))
           "}"
           ""
           "/* SvelteKit Utility Classes */"
           ".container { max-width: 1200px; margin: 0 auto; }"
           ".stack { display: flex; flex-direction: column; }"
           ".stack-horizontal { display: flex; flex-direction: row; }"
           ".grid { display: grid; gap: var(--spacing-md); }"
           ".text-center { text-align: center; }"
           ".text-left { text-align: left; }"
           ".text-right { text-align: right; }"
           ".font-bold { font-weight: bold; }"
           ".font-light { font-weight: 300; }"
           ".opacity-50 { opacity: 0.5; }"
           ".opacity-75 { opacity: 0.75; }"
           ".opacity-100 { opacity: 1; }"])]
    
    (spit (str output-dir "/graindisplay-variables.css") css-variables)
    (println "✅ Exported CSS variables to:" output-dir)))

;; =============================================================================
;; SvelteKit Main Export
;; =============================================================================

(defn -main [& args]
  "Main entry point for SvelteKit export"
  (let [command (first args)
        output-dir (or (second args) "./svelte-export")]
    (case command
      "export" (do
                 (export-svelte-components output-dir)
                 (export-css-variables output-dir))
      "css" (export-css-variables output-dir)
      "components" (export-svelte-components output-dir)
      (do
        (println "GrainDisplay SvelteKit Export")
        (println "")
        (println "USAGE:")
        (println "  clojure -M svelte-ui.clj export [output-dir]")
        (println "  clojure -M svelte-ui.clj css [output-dir]")
        (println "  clojure -M svelte-ui.clj components [output-dir]")))))
