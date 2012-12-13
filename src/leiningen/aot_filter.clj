(ns leiningen.aot-filter
  "Filter unwanted class files after AOT compilation."
  (:require
   [clojure.string :as string])
  (:use
   [clojure.java.io :only [file]]
   [leiningen.core.main :only [debug info]]
   [pathetic.core :only [parse-path relativize render-path]]))

(defn unmunge [f]
  (->
   f
   (string/replace "_" "-")
   (string/replace "/" ".")))

(defn aot-filter
  "Filter unwanted class files after AOT compilation.
   Configuration is through the :aot-include and :aot-exclude project keys.
   This is designed to run as a middleware, and not to be invoked directly."
  [{:keys [aot-include aot-exclude compile-path] :as project} & args]
  (debug
   "AOT Filter "
   (if aot-include (str "include " (pr-str aot-include)) "")
   (if aot-exclude (str "exclude " (pr-str aot-exclude)) ""))
  (debug "Filtering class files in" compile-path)
  (if (or aot-exclude aot-include)
    (letfn [(include? [ns]
              (or
               (not aot-include)
               (some #(re-matches % ns) aot-include)))
            (exclude? [ns]
              (some #(re-matches % ns) aot-exclude))
            (remove? [f]
              (or (exclude? f) (not (include? f))))]
      (doseq [f (file-seq (file compile-path))
              :let [rf (relativize compile-path (.getPath f))
                    ns (unmunge rf)]]
        (if (remove? ns)
          (do
            (debug "Removing" ns)
            (.delete f))
          (debug "Keeping" ns))))
    (info
     "aot-filter unconfigured."
     "Use :aot-include or :aot-exclude in an active profile."))
  project)
