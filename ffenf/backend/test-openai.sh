#!/bin/bash

# OpenAI API Setup and Test Script
# This script helps you set up and test the OpenAI API integration

echo "=== OpenAI API Setup and Test Script ==="
echo ""

# Check if OpenAI API key is set
if [ -z "$OPENAI_API_KEY" ]; then
    echo "❌ OPENAI_API_KEY environment variable is not set!"
    echo ""
    echo "To set up your OpenAI API key:"
    echo "1. Get your API key from: https://platform.openai.com/api-keys"
    echo "2. Set the environment variable:"
    echo "   export OPENAI_API_KEY='your-api-key-here'"
    echo ""
    echo "Or add it to your ~/.bashrc or ~/.zshrc:"
    echo "   echo 'export OPENAI_API_KEY=\"your-api-key-here\"' >> ~/.bashrc"
    echo "   source ~/.bashrc"
    echo ""
    echo "Then restart your terminal and run this script again."
    exit 1
fi

echo "✅ OPENAI_API_KEY is set (length: ${#OPENAI_API_KEY} characters)"
echo ""

# Test the API key by making a simple request
echo "🔍 Testing OpenAI API connection..."
response=$(curl -s -H "Authorization: Bearer $OPENAI_API_KEY" \
                -H "Content-Type: application/json" \
                "https://api.openai.com/v1/models" \
                | head -c 200)

if [[ $response == *"gpt"* ]]; then
    echo "✅ OpenAI API connection successful!"
    echo "Response preview: $response..."
else
    echo "❌ OpenAI API connection failed!"
    echo "Response: $response"
    echo ""
    echo "Please check:"
    echo "1. Your API key is correct"
    echo "2. You have sufficient credits in your OpenAI account"
    echo "3. Your internet connection is working"
    exit 1
fi

echo ""
echo "🚀 Starting the Spring Boot application..."
echo "The application will be available at: http://localhost:8080"
echo ""
echo "Test endpoints:"
echo "- GET http://localhost:8080/ai/test-env (check API key configuration)"
echo "- GET http://localhost:8080/ai/simple-test (basic endpoint test)"
echo "- GET http://localhost:8080/ai/generate/{materialId}?type=summary (generate summary)"
echo ""

# Start the Spring Boot application
cd /Users/akhil/Desktop/Student/ffenf/backend
./mvnw spring-boot:run
