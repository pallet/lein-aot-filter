# lein-aot-filter

A Leiningen plugin to filter AOT compiled class files.

Clojure's compilation process works transitively, so generates many class files
that are not part of the project.  Often, the non-project files can be removed
with (Leiningen)[https://github.com/technomancy/leiningen]'s
`:clean-non-project-classes` project key, but sometimes, finer control is
needed, and is provided by this plugin.

## Usage

Put `[lein-aot-filter "0.1.0"]` into the `:plugins` vector of your
`project.clj` file.

Add an `:aot-include` and/or an `:aot-exclude` key in your `project.clj` file,
with a vector of regex patterns.

The plugin works as a middleware, so is used automatically whenever your project
is built (there is no task to invoke).

## License

Copyright © 2012 Hugo Duncan

Distributed under the Eclipse Public License.
