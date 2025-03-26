package com.example.documentextractionhackathon2025.data

object PromptBuilder {
    /**
     * content can be email content or input text
     */
    fun createPrompt(requestTypes: MutableList<RequestType>, content : String) : String{

        val requestTypeString = constructRequestTypesAndSubtypes(requestTypes)

        val prompt = """
    You are an AI assistant trained to analyze customer emails (In the context of Loan service platform and commercial banking) and classify them into predefined request types and subtypes based on their content.

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

    Expected Output Format:
    
    *Request Type*: [Selected request type]
    
    *Request Subtype*: [Selected request subtype]
    
    *Justification*:  [Explain why this request type and subtype were chosen based on the email content]
    
    *Confidence Score*: []
    
    
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
}