# Waistcoat

Smart middleware for your Clojure REPL.

## Usage

Merge the following into the dependencies of your deps.edn file:

    {waistcoat/waistcoat {:git/url "https://github.com/peterwestmacott/waistcoat.git"
                          :sha     "5461334ddfa6a4b7291632fa1ed702f0619968be"}}

N.B. if you add it as under the :extra-deps of an alias, be sure to run your REPL with that alias!

Merge the following into your .nrepl.edn file:

    {:middleware [waistcoat.tardy/wrap-handler]}

N.B. you may need to create this file at the root of your project/module.