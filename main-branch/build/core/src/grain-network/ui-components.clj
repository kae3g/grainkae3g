(ns grain-network.ui-components
  "Shared Humble UI components for Grain Network applications.
   
   Provides common UI elements, themes, and layouts used across
   all Grain Network desktop applications."
  (:require [io.github.humbleui.ui :as ui]))

(def grain-theme
  "Grain Network theme colors and styles."
  {:primary-color 0xFF8B745E
   :background    0xFF1A1410
   :text          0xFFE8DCC8
   :link          0xFF8B745E
   :code-bg       0xFF2A2420
   :code-fg       0xFFD4C5B0
   :accent        0xFF6B8E23
   :warning       0xFFFFD700
   :error         0xFFDC143C})

(defn grain-button
  "Styled button component with Grain Network theme."
  [{:keys [text on-click disabled?]}]
  (ui/button {:on-click on-click
              :disabled? disabled?}
    (ui/padding 12 8
      (ui/text text))))

(defn grain-card
  "Card component for content display."
  [content]
  (ui/rect {:fill (:background grain-theme)
            :stroke (:primary-color grain-theme)
            :stroke-width 1}
    (ui/padding 16
      content)))

(defn grain-header
  "Header component with Grain Network branding."
  [title subtitle]
  (ui/column
    (ui/padding 16
      (ui/text {:size 24 :weight :bold}
        title))
    (when subtitle
      (ui/padding 8 0
        (ui/text {:size 14 :color (:text grain-theme)}
          subtitle)))
    (ui/rect {:fill (:primary-color grain-theme) :height 2})))

(defn grain-content-viewer
  "Content viewer for markdown and text content."
  [content]
  (ui/scroll
    (ui/padding 16
      (ui/text {:size 14 :color (:text grain-theme)}
        content))))

(defn grain-navigation
  "Navigation component for app navigation."
  [items current-item on-select]
  (ui/row
    (for [item items]
      (ui/button {:on-click #(on-select item)
                  :variant (if (= item current-item) :primary :secondary)}
        (ui/padding 8 4
          (ui/text (:label item)))))))

(defn grain-status-bar
  "Status bar component for app status."
  [status message]
  (ui/rect {:fill (:code-bg grain-theme)}
    (ui/padding 8
      (ui/row
        (ui/text {:size 12 :color (:text grain-theme)}
          (str "Status: " status))
        (ui/fill)
        (ui/text {:size 12 :color (:text grain-theme)}
          message)))))

(defn grain-loading
  "Loading indicator component."
  [message]
  (ui/center
    (ui/column
      (ui/text {:size 16 :color (:text grain-theme)}
        message)
      (ui/padding 8
        (ui/text {:size 12 :color (:primary-color grain-theme)}
          "Loading...")))))

(defn grain-error
  "Error display component."
  [error-message]
  (ui/center
    (ui/column
      (ui/text {:size 16 :color (:error grain-theme)}
        "Error")
      (ui/padding 8
        (ui/text {:size 12 :color (:text grain-theme)}
          error-message)))))

(defn grain-layout
  "Main layout component for Grain Network apps."
  [{:keys [header content sidebar footer]}]
  (ui/column
    (when header header)
    (ui/row
      (when sidebar
        (ui/rect {:fill (:code-bg grain-theme) :width 200}
          sidebar))
      (ui/fill
        content))
    (when footer footer)))
