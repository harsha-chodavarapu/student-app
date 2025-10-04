#!/bin/bash

echo "=== OpenAI API Key Setup ==="
echo ""

# Check if API key is already set
if [ -n "$OPENAI_API_KEY" ]; then
    echo "✅ OpenAI API Key is already set!"
    echo "Key length: ${#OPENAI_API_KEY}"
    echo "Key prefix: ${OPENAI_API_KEY:0:10}..."
    exit 0
fi

echo "❌ OpenAI API Key is not configured."
echo ""
echo "🔑 To get an OpenAI API key:"
echo "1. Go to: https://platform.openai.com/api-keys"
echo "2. Sign in or create an account"
echo "3. Click 'Create new secret key'"
echo "4. Copy the key (starts with 'sk-')"
echo ""
echo "📝 The API key should look like: sk-proj-abc123..."
echo "💰 Make sure you have credits in your OpenAI account"
echo ""

# Ask user to input their API key
echo "Please enter your OpenAI API key:"
read -s openai_key

if [ -z "$openai_key" ]; then
    echo "❌ No API key provided. Exiting."
    exit 1
fi

if [[ ! $openai_key =~ ^sk- ]]; then
    echo "❌ Invalid API key format. Should start with 'sk-'"
    exit 1
fi

echo ""
echo "🔧 Setting up environment variable..."

# Add to zshrc (since you're using zsh)
echo "export OPENAI_API_KEY=\"$openai_key\"" >> ~/.zshrc

# Set for current session
export OPENAI_API_KEY="$openai_key"

echo "✅ OpenAI API Key configured successfully!"
echo "Key length: ${#OPENAI_API_KEY}"
echo ""
echo "🔄 To apply changes:"
echo "   source ~/.zshrc"
echo ""
echo "🧪 Test your setup:"
echo "   cd /Users/akhil/Desktop/Student/ffenf/backend"
echo "   ./test-openai.sh"
echo ""
echo "🚀 Then restart the application:"
echo "   ./mvnw spring-boot:run"
