# AIGeminiAgents — Multi-Agent Chat with Function Calling

This project implements an **AI-powered multi-agent chat system** using **Google Gemini LLMs**,  
designed to handle different types of user inquiries through specialized agents.  

It is built with **Spring Boot** and **Java**, leveraging the **Spring AI Chat Client**  
for interaction with the **Gemini API**.  

> The project uses the **free Gemini 2.0 Flash-Lite** model from [Google AI Studio](https://aistudio.google.com/).  
> It’s fast, intelligent, and provides a panel showing token cost and response errors.

---

## Overview

The system currently includes two main agents:

### **AgentA - Technical Support Expert**
- Handles **technical queries** using provided documentation and session history.  
- Generates **precise, context-aware responses**.  
- Uses context from files inside the `resources/docs` directory (simple data for testing).

### **AgentB - Billing Specialist**
- Manages **billing-related questions** (currently only prices).  
- Supports **function calling** to automate:
  - Support ticket creation  
  - Return form filling  
- Function call responses (**JSON**) are saved to files —  
  for now, they simulate real tickets or forms.

---

## Key Features

**Multi-turn conversation management**  
Maintains and passes conversation history to agents for coherent, contextual responses.  

**Function calling**  
Agents can generate **structured JSON instructions** to invoke backend functions automatically.  

**Extensible architecture**  
Easily add or modify agents with specialized logic.

---

## Setup

### **Google Gemini API Key**
Replace the Gemini API key in `application.properties` with the key obtained from [AI Studio](https://aistudio.google.com/).

### **Build and Run**
Use **Maven** or your preferred build tool to compile and run the Spring Boot application:

```bash
mvn spring-boot:run
