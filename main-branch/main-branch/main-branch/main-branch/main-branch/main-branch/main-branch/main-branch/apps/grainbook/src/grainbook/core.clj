(ns grainbook.core
  "Main application logic for grainbook - Grain Network content reader.
   
   Provides the core functionality for reading and managing Grain Network
   content using Humble UI for native desktop interface."
  (:require [io.github.humbleui.ui :as ui]
            [grain-network.ui-components :as components]
            [grain-network.content :as content]
            [grain-network.navigation :as nav]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(def app-state
  "Application state for grainbook."
  (atom {:current-content nil
         :content-index []
         :navigation-path []
         :search-query ""
         :sidebar-visible true}))

(defn load-content-index
  "Load the content index from the content directory."
  []
  (let [content-path "content"]
    (when (.exists (io/file content-path))
      (content/build-content-index content-path))))

(defn update-app-state!
  "Update application state with new values."
  [updates]
  (swap! app-state merge updates))

(defn get-current-content
  "Get the currently displayed content."
  []
  (:current-content @app-state))

(defn set-current-content!
  "Set the currently displayed content."
  [content]
  (update-app-state! {:current-content content}))

(defn get-content-index
  "Get the content index."
  []
  (:content-index @app-state))

(defn set-content-index!
  "Set the content index."
  [index]
  (update-app-state! {:content-index index}))

(defn get-navigation-path
  "Get the current navigation path."
  []
  (:navigation-path @app-state))

(defn set-navigation-path!
  "Set the navigation path."
  [path]
  (update-app-state! {:navigation-path path}))

(defn get-search-query
  "Get the current search query."
  []
  (:search-query @app-state))

(defn set-search-query!
  "Set the search query."
  [query]
  (update-app-state! {:search-query query}))

(defn toggle-sidebar!
  "Toggle sidebar visibility."
  []
  (update-app-state! {:sidebar-visible (not (:sidebar-visible @app-state))}))

(defn search-content
  "Search content by query."
  [query]
  (let [index (get-content-index)]
    (if (str/blank? query)
      index
      (content/search-content index query))))

(defn navigate-to-content
  "Navigate to specific content."
  [content-item]
  (set-current-content! content-item)
  (set-navigation-path! (nav/parse-path (:path content-item))))

(defn initialize-app!
  "Initialize the application."
  []
  (let [index (load-content-index)]
    (set-content-index! index)
    (when (seq index)
      (set-current-content! (first index)))))

(defn -main
  "Main entry point for grainbook application."
  [& args]
  (initialize-app!)
  (ui/start-app!
    (ui/window
      {:title "grainbook - Grain Network Content Reader"
       :width 1200
       :height 800}
      #'grainbook.ui/app)))
