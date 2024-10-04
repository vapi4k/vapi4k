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

package com.vapi4k.dsl.assistant

import com.vapi4k.api.assistant.SuccessEvaluationRubricType

interface AnalysisPlanProperties {
  /**
  <p>This is the prompt that's used to extract structured data from the call. The output is stored in <code>call.analysis.structuredData</code>.
  <br>Disabled by default.
  <br>You can use this standalone or in combination with <code>structuredDataSchema</code>. If both are provided, they are concatenated into appropriate instructions.
  </p>
   */
  var structuredDataPrompt: String

  /**
  <p>This is how long the request is tried before giving up. When request times out, <code>call.analysis.structuredData</code> will be empty. Increasing this timeout will delay the end of call report.
  <br>Default is 5 seconds.
  </p>
   */
  var structuredDataRequestTimeoutSeconds: Double

  /**
  <p>This is the prompt that's used to evaluate if the call was successful. The output is stored in <code>call.analysis.successEvaluation</code>.
  <br>Default is "You are an expert call evaluator. You will be given a transcript of a call and the system prompt of the AI participant.
  Determine if the call was successful based on the objectives inferred from the system prompt. DO NOT return anything except the result.".
  <br>Set to '' or 'off' to disable.
  <br>You can use this standalone or in combination with <code>successEvaluationRubric</code>. If both are provided, they areconcatenated into appropriate instructions.
  </p>
   */
  var successEvaluationPrompt: String

  /**
  <p>This is how long the request is tried before giving up. When request times out, <code>call.analysis.successEvaluation will be empty</code>. Increasing this timeout will delay the end of call report.
  <br>Default is 5 seconds.
  </p>
   */
  var successEvaluationRequestTimeoutSeconds: Double

  /**
  <p>This enforces the rubric of the evaluation. The output is stored in call.analysis.successEvaluation.
  <br>Options include:
  <li>'NumericScale': A scale of 1 to 10.
  <li>'DescriptiveScale': A scale of Excellent, Good, Fair, Poor.
  <li>'Checklist': A checklist of criteria and their status.
  <li>'Matrix': A grid that evaluates multiple criteria across different performance levels.
  <li>'PercentageScale': A scale of 0% to 100%.
  <li>'LikertScale': A scale of Strongly Agree, Agree, Neutral, Disagree, Strongly Disagree.
  <li>'AutomaticRubric': Automatically break down evaluation into several criteria, each with its own score.
  <li>'PassFail': A simple 'true' if call passed, 'false' if not.
  For 'Checklist' and 'Matrix', provide the criteria in successEvaluationPrompt.
  <br>Default is 'PassFail' if <code>successEvaluationPrompt</code> is not provided, and null if <code>successEvaluationPrompt</code> is provided.
  <br>You can use this standalone or in combination with <code>successEvaluationPrompt</code>. If both are provided, they are concatenated into appropriate instructions.
  </p>
   */
  var successEvaluationRubric: SuccessEvaluationRubricType

  /**
  <p>This is the prompt that's used to summarize the call. The output is stored in <code>call.analysis.summary</code>.
  <br>Default is "You are an expert note-taker. You will be given a transcript of a call. Summarize the call in 2-3 sentences. DO NOT return anything except the summary.".
  <br>Set to '' or 'off' to disable.
  </p>
   */
  var summaryPrompt: String

  /**
  <p>This is how long the request is tried before giving up. When request times out, <code>call.analysis.summary</code> will be empty. Increasing this timeout will delay the end of call report.
  <br>Default is 5 seconds.
  </p>
   */
  var summaryRequestTimeoutSeconds: Double
}
