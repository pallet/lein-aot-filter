(ns lein-aot-filter.plugin)

(defn middleware
  [project]
  (update-in project [:prep-tasks] concat ["aot-filter"]))
