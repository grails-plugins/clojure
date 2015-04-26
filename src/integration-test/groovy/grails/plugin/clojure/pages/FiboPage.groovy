package grails.plugin.clojure.pages

import geb.Module
import geb.Page

class FiboPage extends Page {

    static url = "/demo/fibo"

    static at = {
        title == 'Grails And Clojure Fibonacci'
    }

    static content = {
        ol {$("ol", 0)}
        liRows(required: false) {ol.find("li")}
        liRow {module LiRow, liRows[it]}
    }
}

class LiRow extends Module {
    static content = {
        li {$("li", it)}
        liText {li(it).text()}
    }
}