(ns grainbook.ui
  "User interface for grainbook - Grain Network content reader.
   
   Provides the Humble UI interface for reading and navigating
   Grain Network content."
  (:require [io.github.humbleui.ui :as ui]
            [grain-network.ui-components :as components]
            [grainbook.core :as core]
            [clojure.string :as str]))

(defn sidebar
  "Sidebar component for navigation."
  []
  (let [index (core/get-content-index)
        query (core/get-search-query)
        filtered-content (core/search-content query)]
    (ui/column
      (components/grain-header "grainbook" "Grain Network Content Reader")
      (ui/padding 8
        (ui/text-field
          {:placeholder "Search content..."
           :value query
           :on-change core/set-search-query!}))
      (ui/scroll
        (ui/column
          (for [item filtered-content]
            (ui/button
              {:on-click #(core/navigate-to-content item)}
              (ui/padding 8 4
                (ui/text (:path item))))))))))

(defn content-viewer
  "Content viewer component."
  []
  (let [content (core/get-current-content)]
    (if content
      (ui/column
        (components/grain-header 
          (or (:title (:metadata content)) (:path content))
          (or (:description (:metadata content)) "Grain Network Content"))
        (ui/scroll
          (components/grain-content-viewer (:content content))))
      (ui/center
        (components/grain-loading "Loading content...")))))

(defn main-layout
  "Main application layout."
  []
  (let [sidebar-visible (:sidebar-visible @core/app-state)]
    (components/grain-layout
      {:sidebar (when sidebar-visible (sidebar))
       :content (content-viewer)
       :footer (components/grain-status-bar 
                "Ready" 
                (str "Content items: " (count (core/get-content-index)))}))))

(defn app
  "Main application component."
  []
  (main-layout))
