# OpenAI API Integration - Setup Guide

This guide will help you set up and test the OpenAI API integration for generating accurate summaries and flashcards from PDF documents.

## 🚀 Quick Start

### 1. Set Up OpenAI API Key

First, you need to get an OpenAI API key:

1. Go to [OpenAI Platform](https://platform.openai.com/api-keys)
2. Create a new API key
3. Copy the key (it starts with `sk-`)

### 2. Set Environment Variable

**Option A: Temporary (for current session)**
```bash
export OPENAI_API_KEY="sk-your-api-key-here"
```

**Option B: Permanent (recommended)**
```bash
echo 'export OPENAI_API_KEY="sk-your-api-key-here"' >> ~/.bashrc
source ~/.bashrc
```

**Option C: For macOS with zsh**
```bash
echo 'export OPENAI_API_KEY="sk-your-api-key-here"' >> ~/.zshrc
source ~/.zshrc
```

### 3. Test the Setup

Run the test script:
```bash
cd /Users/akhil/Desktop/Student/ffenf/backend
./test-openai.sh
```

## 🔧 What's Been Improved

### Enhanced AI Prompts
- **Comprehensive Summaries**: The AI now generates structured summaries with clear sections, key concepts, and important details
- **Better Flashcards**: Creates 8-12 high-quality flashcards with varied difficulty levels
- **Educational Focus**: Prompts are designed to maximize learning effectiveness

### Robust Error Handling
- **API Key Validation**: Proper checking for OpenAI API key configuration
- **File Upload Validation**: Ensures PDF files exist before processing
- **Timeout Management**: 5-minute timeout with proper polling for AI responses
- **Error Recovery**: Graceful handling of API failures with informative error messages

### Improved Architecture
- **Configuration Management**: Centralized OpenAI configuration with `OpenAiConfig`
- **Service Separation**: Dedicated `OpenAiFileService` for file operations
- **Type Safety**: Fixed all linting warnings and improved code quality

## 📋 API Endpoints

### Test Endpoints
- `GET /ai/test-env` - Check OpenAI API key configuration
- `GET /ai/simple-test` - Basic endpoint test
- `GET /ai/test` - AI endpoint test

### Main Functionality
- `GET /ai/generate/{materialId}?type=summary` - Generate summary
- `GET /ai/generate/{materialId}?type=flashcards` - Generate flashcards
- `GET /ai/generate/{materialId}?type=both` - Generate both summary and flashcards
- `GET /ai/job/{jobId}` - Check job status
- `GET /ai/material/{materialId}` - Get material AI content

## 🎯 How It Works

1. **File Upload**: PDF files are uploaded to OpenAI's file storage
2. **AI Processing**: Uses OpenAI's Assistants API with GPT-4o model
3. **Content Generation**: Creates structured summaries and educational flashcards
4. **Storage**: Results are saved to the database for future access

## 🔍 Testing the Integration

### 1. Check Configuration
```bash
curl http://localhost:8080/ai/test-env
```

Expected response:
```json
{
  "apiKeySet": true,
  "apiKeyLength": 51,
  "apiKeyPrefix": "sk-proj-abc",
  "openAiConfigured": true
}
```

### 2. Test Summary Generation
```bash
curl "http://localhost:8080/ai/generate/your-material-id?type=summary"
```

### 3. Test Flashcards Generation
```bash
curl "http://localhost:8080/ai/generate/your-material-id?type=flashcards"
```

## 🛠️ Troubleshooting

### Common Issues

**1. "OpenAI API key not configured"**
- Make sure `OPENAI_API_KEY` environment variable is set
- Restart your terminal after setting the variable
- Check the key format (should start with `sk-`)

**2. "Failed to upload file to OpenAI"**
- Check your OpenAI account has sufficient credits
- Verify the PDF file exists and is readable
- Ensure file size is under OpenAI's limits (50MB)

**3. "Run did not complete within timeout period"**
- The AI processing is taking longer than 5 minutes
- Check OpenAI service status
- Try with a smaller PDF file

**4. "Failed to create assistant"**
- Check your OpenAI API key permissions
- Ensure you have access to the Assistants API
- Verify your account has sufficient credits

### Debug Steps

1. **Check API Key**: Run `echo $OPENAI_API_KEY` to verify it's set
2. **Test Connection**: Use the `/ai/test-env` endpoint
3. **Check Logs**: Look at the application logs for detailed error messages
4. **Verify File**: Ensure the PDF file exists and is accessible

## 💡 Tips for Better Results

1. **PDF Quality**: Use high-quality PDFs with clear text
2. **File Size**: Smaller files process faster (under 10MB recommended)
3. **Content Type**: Academic materials work best for summaries and flashcards
4. **Retry Logic**: The system includes retry mechanisms for failed requests

## 🔒 Security Notes

- Never commit your API key to version control
- Use environment variables for API keys
- Monitor your OpenAI usage to avoid unexpected charges
- Consider implementing rate limiting for production use

## 📊 Monitoring

The system logs detailed information about:
- File upload status
- AI processing progress
- Error messages and stack traces
- API response times

Check the application logs for detailed debugging information.

---

**Need Help?** Check the application logs or test the `/ai/test-env` endpoint to diagnose issues.
