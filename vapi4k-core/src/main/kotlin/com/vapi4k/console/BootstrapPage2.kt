/*
 * Copyright © 2024 Matthew Ambrose (mattbobambrose@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package com.vapi4k.console

import com.vapi4k.common.Constants.STATIC_BASE
import kotlinx.html.ButtonType
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.aside
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.i
import kotlinx.html.id
import kotlinx.html.li
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.script
import kotlinx.html.span
import kotlinx.html.title
import kotlinx.html.ul

internal object BootstrapPage2 {
  fun HTML.bootstrapPage2() {
    head {
      meta {
        charset = "UTF-8"
      }
      meta {
        attributes["http-equiv"] = "X-UA-Compatible"
        content = "IE=edge"
      }
      meta {
        name = "viewport"
        content = "width=device-width, initial-scale=1.0"
      }
      title {
      }
      link {
        rel = "stylesheet"
        href = "https://cdn.lineicons.com/4.0/lineicons.css"
      }
      link {
        attributes["integrity"] = "sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
        attributes["crossorigin"] = "anonymous"
        rel = "stylesheet"
        href = "https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      }
      link {
        rel = "stylesheet"
        href = "$STATIC_BASE/css/styles2.css"
      }
      link {
        rel = "stylesheet"
        href = "$STATIC_BASE/css/sidebar-toggle.css"
      }
    }
    body {
      div {
        classes += "wrapper"
        aside {
          id = "sidebar"
          div {
            classes += "d-flex"
            button {
              classes += "toggle-btn"
              type = ButtonType.button
              i("lni lni-grid-alt") {
              }
            }
            div {
              classes += "sidebar-logo"
              a {
                href = "#"
                +"Vapi4k Admin"
              }
            }
          }
          ul {
            classes += "sidebar-nav"

            li {
              classes += "sidebar-item"
              a {
                classes += "sidebar-link"
                href = "#"
                i("lni lni-user") {
                }
                span { +"Profile" }
              }
            }

            li {
              classes += "sidebar-item"
              a {
                classes += "sidebar-link"
                href = "#"
                i { classes = setOf("lni", "lni-protection") }
                span { +"Task" }
              }
            }

            li {
              classes += "sidebar-item"
              a {
                classes = setOf("sidebar-link", "collapsed", "has-dropdown")
                attributes["data-bs-toggle"] = "collapse"
                attributes["data-bs-target"] = "#auth"
                attributes["aria-expanded"] = "false"
                attributes["aria-controls"] = "auth"
                href = "#"
                i { classes = setOf("lni", "lni-protection") }
                span { +"Auth" }
              }
              ul("sidebar-dropdown list-unstyled collapse") {
                id = "auth"
                attributes["data-bs-parent"] = "#sidebar"

                li {
                  classes += "sidebar-item"
                  a {
                    classes += "sidebar-link"
                    href = "#"
                    +"Login"
                  }
                }
                li {
                  classes += "sidebar-item"
                  a {
                    classes += "sidebar-link"
                    href = "#"
                    +"Register"
                  }
                }
              }
            }

            li {
              classes += "sidebar-item"
              a {
                classes = setOf("sidebar-link", "collapsed", "has-dropdown")
                attributes["data-bs-toggle"] = "collapse"
                attributes["data-bs-target"] = "#multi"
                attributes["aria-expanded"] = "false"
                attributes["aria-controls"] = "multi"
                href = "#"
                i { classes = setOf("lni", "lni-protection") }
                span { +"Multi Level" }
              }
              ul {
                classes = setOf("sidebar-dropdown", "list-unstyled", "collapse")
                id = "multi"
                attributes["data-bs-parent"] = "#sidebar"
                li("sidebar-item") {
                  a {
                    classes = setOf("sidebar-link", "collapsed")
                    attributes["data-bs-toggle"] = "collapse"
                    attributes["data-bs-target"] = "#multi-two"
                    attributes["aria-expanded"] = "false"
                    attributes["aria-controls"] = "multi-two"
                    href = "#"
                    +"Two Links"
                  }
                  ul("sidebar-dropdown list-unstyled collapse") {
                    id = "multi-two"
                    li("sidebar-item") {
                      a {
                        classes += "sidebar-link"
                        href = "#"
                        +"Link 1"
                      }
                    }
                    li {
                      classes += "sidebar-item"
                      a {
                        classes += "sidebar-link"
                        href = "#"
                        +"Link 2"
                      }
                    }
                  }
                }
              }
            }
            li {
              classes += "sidebar-item"
              a {
                classes += "sidebar-link"
                href = "#"
                i {
                  classes = setOf("lni", "lni-protection")
                }
                span { +"Notification" }
              }
            }
            li {
              classes += "sidebar-item"
              a {
                classes += "sidebar-link"
                href = "#"
                i {
                  classes = setOf("lni", "lni-protection")
                }
                span { +"Setting" }
              }
            }
          }
          div {
            classes += "sidebar-footer"
            a {
              classes += "sidebar-link"
              href = "#"
              i {
                classes = setOf("lni", "lni-protection")
              }
              span { +"Logout" }
            }
          }
        }
        div {
          classes = setOf("main", "p-3")
          div {
            classes += "text-center"
            h1 { +"Sidebar Bootstrap 5" }
          }
        }
      }

      script {
        attributes["crossorigin"] = "anonymous"
        src = "https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity = "sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
      }

      script { src = "$STATIC_BASE/js/sidebar-toggle.js" }
    }
  }
}
