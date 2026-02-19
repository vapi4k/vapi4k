/*
 * Copyright © 2024 Matthew Ambrose
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

package com.vapi4k.plugin

import com.github.pambrose.common.json.defaultJsonConfig
import com.github.pambrose.common.json.toJsonElement
import com.github.vapi4k.vapi4k.BuildConfig
import com.vapi4k.api.vapi4k.Vapi4kConfig
import com.vapi4k.common.Constants.APP_NAME
import com.vapi4k.common.Constants.APP_TYPE
import com.vapi4k.common.Constants.AUTH_BASIC
import com.vapi4k.common.Constants.BS_BASE
import com.vapi4k.common.Constants.PRISM_BASE
import com.vapi4k.common.Constants.STATIC_BASE
import com.vapi4k.common.CoreEnvVars.isProduction
import com.vapi4k.common.CoreEnvVars.loadCoreEnvVars
import com.vapi4k.common.CoreEnvVars.vapi4kBaseUrl
import com.vapi4k.common.Endpoints.ADMIN_ENV_PATH
import com.vapi4k.common.Endpoints.ADMIN_LOG_ENDPOINT
import com.vapi4k.common.Endpoints.ADMIN_PATH
import com.vapi4k.common.Endpoints.ADMIN_VERSION_PATH
import com.vapi4k.common.Endpoints.CACHES_PATH
import com.vapi4k.common.Endpoints.CLEAR_CACHES_PATH
import com.vapi4k.common.Endpoints.ENV_PATH
import com.vapi4k.common.Endpoints.INVOKE_TOOL_PATH
import com.vapi4k.common.Endpoints.PING_PATH
import com.vapi4k.common.Endpoints.VALIDATE_PATH
import com.vapi4k.common.Endpoints.VERSION_PATH
import com.vapi4k.common.Version
import com.vapi4k.common.Version.Companion.versionDesc
import com.vapi4k.console.AdminLog.adminLogWs
import com.vapi4k.console.AdminPage.adminPage
import com.vapi4k.console.BootstrapPage2.bootstrapPage2
import com.vapi4k.console.ConsoleInfo.envVarsInfo
import com.vapi4k.console.ConsoleInfo.versionInfo
import com.vapi4k.console.InvokeTool.invokeTool
import com.vapi4k.console.ValidateApplication.validateApplication
import com.vapi4k.dsl.vapi4k.AbstractApplicationImpl.Companion.containsPath
import com.vapi4k.dsl.vapi4k.Vapi4kConfigImpl
import com.vapi4k.envvar.EnvVar.Companion.jsonEnvVarValues
import com.vapi4k.envvar.EnvVar.Companion.logEnvVarValues
import com.vapi4k.plugin.Vapi4kServer.logger
import com.vapi4k.server.AdminJobs.startCacheCleaningThread
import com.vapi4k.server.AdminJobs.startCallbackThread
import com.vapi4k.server.CacheActions.cachesRequest
import com.vapi4k.server.CacheActions.clearCaches
import com.vapi4k.server.InboundCallActions.inboundCallRequest
import com.vapi4k.server.OutboundCallAndWebActions.addArgsAndMessage
import com.vapi4k.server.OutboundCallAndWebActions.buildRequestArg
import com.vapi4k.server.OutboundCallAndWebActions.outboundCallAndWebRequest
import com.vapi4k.server.defaultKtorConfig
import com.vapi4k.server.installContentNegotiation
import com.vapi4k.utils.MiscUtils.getBanner
import com.vapi4k.utils.MiscUtils.removeEnds
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.MethodNotAllowed
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.ApplicationPlugin
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStarting
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.application.call
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.auth.authenticate
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.path
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.websocket.webSocket
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.json.buildJsonObject

@Version(
  version = BuildConfig.VERSION,
  releaseDate = BuildConfig.RELEASE_DATE,
  buildTime = BuildConfig.BUILD_TIME,
)
object Vapi4kServer {
  val logger = KotlinLogging.logger {}
}

val Vapi4k: ApplicationPlugin<Vapi4kConfig> =
  createApplicationPlugin(
    name = "Vapi4k",
    createConfiguration = { Vapi4kConfigImpl() },
  ) {
    loadCoreEnvVars()

    val config =
      Vapi4kConfigImpl.config.apply {
        applicationConfig = environment.config
        callbackChannel = Channel(Channel.UNLIMITED)
      }

    with(logger) {
      info { getBanner("banners/vapi4k-server.banner", logger) }
      info { Vapi4kServer::class.versionDesc() }
    }

    logEnvVarValues { logger.info { it } }

    startCallbackThread(config)
    startCacheCleaningThread(config)

    with(application) {
      monitor.apply {
        val name = Vapi4kServer::class.simpleName.orEmpty()
        subscribe(ApplicationStarting) { it.environment.log.info("$name is starting") }
        subscribe(ApplicationStarted) { it.environment.log.info("$name is started at $vapi4kBaseUrl") }
        subscribe(ApplicationStopped) { it.environment.log.info("$name is stopped") }
        subscribe(ApplicationStopping) { it.environment.log.info("$name is stopping") }
      }

      val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
      defaultKtorConfig(appMicrometerRegistry)

      routing {
        staticResources(STATIC_BASE, "core_static")
        staticResources(BS_BASE, "bootstrap")
        staticResources(PRISM_BASE, "prism")

        get(PING_PATH) { call.respondText("pong") }

        get(VERSION_PATH) { call.respondText(Application.Json) { Vapi4kServer::class.versionDesc(true) } }

        get("/bootstrap") { call.respondHtml { bootstrapPage2() } }

        if (!isProduction) {
          get("/") { call.respondRedirect(ADMIN_PATH) }

          route(ENV_PATH) {
            installContentNegotiation { defaultJsonConfig() }
            get { call.respond(jsonEnvVarValues()) }
          }

//          route(METRICS_PATH) {
//            installContentNegotiation()
//            get { call.respond(appMicrometerRegistry.scrape()) }
//          }

          route(CACHES_PATH) {
            installContentNegotiation()
            get { cachesRequest(config) }
          }

          route(CLEAR_CACHES_PATH) {
            installContentNegotiation()
            get { clearCaches(config) }
          }

          authenticate(AUTH_BASIC) {
            get(ADMIN_PATH) { call.respondHtml { adminPage(config) } }

            get("$VALIDATE_PATH/{$APP_TYPE}/{$APP_NAME}") { call.respondText(validateApplication(config)) }

            get(INVOKE_TOOL_PATH) { call.respondText(invokeTool(config)) }

            get(ADMIN_ENV_PATH) { call.respondText(envVarsInfo()) }

            get(ADMIN_VERSION_PATH) { call.respondText(versionInfo()) }

            webSocket(ADMIN_LOG_ENDPOINT) { adminLogWs() }
          }
        }

        // Process Inbound Call requests
        config.inboundCallApplications.forEach { application ->
          val path = "/${application.fullServerPath}"
          route(path) {
            logger.info { """Adding POST endpoint "$path" for ${application.applicationType.displayName} """ }
            installContentNegotiation()
            get { call.respondText("${this@route.parent} requires a post request", status = MethodNotAllowed) }
            post { inboundCallRequest(config, application) }
          }
        }

        // Process Outbound Call and Web requests
        (config.outboundCallApplications + config.webApplications)
          .forEach { application ->
            val path = "/${application.fullServerPath}"
            route(path) {
              installContentNegotiation()
              logger.info { """Adding GET and POST endpoints "$path" for ${application.applicationType.displayName}""" }
              get {
                val request = buildJsonObject { addArgsAndMessage(call) }
                outboundCallAndWebRequest(config, application, request)
              }
              post {
                val json = call.receive<String>().toJsonElement(config.isVerbose)
                val request = buildRequestArg(json)
                outboundCallAndWebRequest(config, application, request)
              }
            }
          }
      }

      intercept(ApplicationCallPipeline.Call) {
        proceed()
        if (call.response.status() == HttpStatusCode.NotFound) {
          // See if user forgot the inboundCall prefix in the path
          val path = call.request.path().removeEnds("/")
          if (config.inboundCallApplications.containsPath(path)) {
            logger.info {
              "/$path is a valid inboundCallApplication{} serverPath value. " +
                "Did you mean to use \"/inboundCall/$path\" instead of \"/$path\" as the Vapi Server URL in the Vapi dashboard?"
            }
          }
        }
      }
    }
  }
