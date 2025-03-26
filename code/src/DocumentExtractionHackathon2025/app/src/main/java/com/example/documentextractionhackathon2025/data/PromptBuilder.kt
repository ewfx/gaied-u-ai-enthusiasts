package com.example.documentextractionhackathon2025.data

object PromptBuilder {


    /**
     * content can be email content or ipput text
     */
    fun createPrompt(requestTypes: MutableList<RequestType>, content : String) : String{

        val requestTypeString = constructRequestTypesAndSubtypes(requestTypes)

        val prompt = """
    You are an AI assistant trained to analyze customer emails and classify them into predefined request types and subtypes based on their content.

    **Instructions:**
    1. **Understand Request Types and Subtypes:**
       - Below is a list of request types and their definitions.
       - Each request type has multiple subtypes, each with its own definition.
       - Carefully read and understand the definitions before analyzing the email.

    2. **Analyze the Email Content:**
       - Read the provided email content carefully.
       - Identify the most relevant request type based on the content.
       - If applicable, identify the most relevant request subtype.
       - Provide a justification for why you selected the specific request type and subtype.

    **Predefined Request Types and Subtypes:**
    $requestTypeString

    **Email Content:**
    ```
    [$content]
    ```

    **Expected Output Format:**
    ```
    Request Type: [Selected request type]
    Request Subtype: [Selected request subtype]
    Justification: [Explain why this request type and subtype were chosen based on the email content]
    Confidence Score: []
    ```
    """.trimIndent()

        return prompt
    }


    private fun constructRequestTypesAndSubtypes(requestTypes: MutableList<RequestType>): String {

        val finalRequestTypeString = StringBuilder()
        val requestTypeStr = StringBuilder(""" """)
        for (i in requestTypes.indices) {
            val requestType = requestTypes[i]
            val subtypeStr = constructSubtypes(requestType.subtype)
            requestTypeStr.append("""
        - **Request Type ${i+1}: ${requestType.name} **
      - Definition: ${requestType.definition}
        $subtypeStr
        
    """.trimIndent()) // End of requestType

        }// End OF Loop

        finalRequestTypeString.append(requestTypeStr)

        return finalRequestTypeString.toString()
    }

    private fun constructSubtypes(subtypes : List<RequestSubType>?): StringBuilder{
        if(subtypes.isNullOrEmpty()) return StringBuilder("")

        val subTypesString = StringBuilder("""
               - **Subtypes:**
               
           """.trimIndent())
        subtypes.forEach { eachSubtype ->
            subTypesString.appendLine(
                """- **${eachSubtype.name}:** ${eachSubtype.definition}
               """.trimIndent()
            ) // End of subtypes
        }
        return subTypesString
    }


    val temp_PROMPT = """
    You are an AI assistant trained to analyze customer emails and classify them into predefined request types and subtypes based on their content.

    **Instructions:**
    1. **Understand Request Types and Subtypes:**
       - Below is a list of request types and their definitions.
       - Each request type has multiple subtypes, each with its own definition.
       - Carefully read and understand the definitions before analyzing the email.

    2. **Analyze the Email Content:**
       - Read the provided email content carefully.
       - Identify the most relevant request type based on the content.
       - If applicable, identify the most relevant request subtype.
       - Provide a justification for why you selected the specific request type and subtype.

    **Predefined Request Types and Subtypes:**
    - **Request Type 1: Account Issues**
      - Definition: Any issue related to logging in, resetting passwords, or accessing an account.
      - **Subtypes:**
        - **Forgot Password:** User has forgotten their password and needs a reset.
        - **Account Locked:** User's account is locked due to multiple failed attempts.
        - **Other Login Issues:** Any other login-related problems.

    - **Request Type 2: Payment Issues**
      - Definition: Any issue related to making payments, failed transactions, or refunds.
      - **Subtypes:**
        - **Failed Transaction:** A payment was attempted but did not go through.
        - **Refund Request:** User is requesting a refund for a payment.
        - **Billing Error:** User was charged incorrectly or unexpectedly.

    - **Request Type 3: Service Requests**
      - Definition: Requests related to requesting a new service, modification, or cancellation of an existing service.
      - **Subtypes:**
        - **New Service Request:** User wants to enroll in a new service.
        - **Modify Existing Service:** User wants to change an existing service.
        - **Cancel Service:** User wants to terminate a service.

    **Email Content:**
    ```
    [Provide the actual email content here]
    ```

    **Expected Output Format:**
    ```
    Request Type: [Selected request type]
    Request Subtype: [Selected request subtype]
    Justification: [Explain why this request type and subtype were chosen based on the email content]
    ```
""".trimIndent()

}