AIGeminiAgents - Multi-Agent Chat with Function Calling

This project implements an AI-powered multi-agent chat system using Google Gemini LLMs, designed to handle different types of user inquiries through specialized agents. 
It is built with Spring Boot and Java, leveraging the Spring AI Chat Client for interaction with the Gemini API (Free gemini 2.0 flash-lite from https://aistudio.google.com/ is fast and inteligent enough for this project and it has panel which shows how much particular response cost as well as response errors which can appear).

Overview

The system currently includes two main agents:

AgentA (Technical Support Expert): Handles technical queries using provided documentation and session history to generate precise, context-aware responses (Context from filrs inside \resources\docs - simple data just for the tests).
AgentB (Billing Specialist): Manages billing-related questions (for now only prices), supports function calling for automated tasks such as creating support tickets or filling return forms based on user inputs (after function calling the response (JSON) is saved inside files - for now yet it can be used as a real ticket ot form).

Key Features

Multi-turn conversation management: Conversation history is maintained and passed to the agents to provide context for more coherent and relevant answers.
Function calling: Agents can generate structured JSON instructions to invoke backend functions automatically, enabling seamless interaction such as ticket creation or form filling.
Extensible architecture: Agents’ logic is modularized, allowing easy expansion or addition of new specialized agents in the future.

Setup

Google Gemini API Key - Replace the Gemini API key in your application configuration (application.properties) with the key obtained from AI Studio.

Build and run - Use Maven or your preferred build tool to compile and run the Spring Boot application.

Endpoints - You can test the API using the provided Postman collection AIGeminiAgentsAPI.postman_collection.json where is only endpoint - /chat/ask
/chat/ask - POST endpoint to send user questions along with conversation history.
it contains:
{
    "userQuestion":"Question",
    "clarificationAnswer":"angular/decision tree/esp32/Network Monitoring system",
    "sessionHistory":"In the future it should be filled by frontend application"
}

Notes

Ensure to maintain prompt formatting as shown in the source code to allow the model to recognize function calls.
Responses are filtered and parsed to safely handle JSON function call instructions or fallback to text replies.
History management is crucial for multi-turn context, make sure clients maintain and send it correctly.
