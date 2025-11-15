#!/bin/bash

BASE_URL="http://localhost:8080/api/profiles"

echo "=== Testing User Profiles API ==="
echo ""

# Test 1: Create Profile
echo "1. Creating profile..."
RESPONSE=$(curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","bio":"Test bio","phone":"1234567890","location":"Test City"}')
echo "$RESPONSE" | python3 -m json.tool

# Extract ID
ID=$(echo $RESPONSE | python3 -c "import sys, json; print(json.load(sys.stdin)['id'])" 2>/dev/null)

if [ -z "$ID" ]; then
    echo "ERROR: Failed to create profile"
    exit 1
fi

echo ""
echo "Created profile with ID: $ID"
echo ""

# Test 2: Get All
echo "2. Getting all profiles..."
curl -s $BASE_URL | python3 -m json.tool
echo ""

# Test 3: Get by ID
echo "3. Getting profile by ID: $ID"
curl -s $BASE_URL/$ID | python3 -m json.tool
echo ""

# Test 4: Update
echo "4. Updating profile..."
curl -s -X PUT $BASE_URL/$ID \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated User","bio":"Updated bio"}' | python3 -m json.tool
echo ""

# Test 5: Validation - Duplicate Email
echo "5. Testing duplicate email validation..."
DUPLICATE_RESPONSE=$(curl -s -w "\nHTTP_STATUS:%{http_code}" -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{"name":"Another User","email":"test@example.com"}')
HTTP_STATUS=$(echo "$DUPLICATE_RESPONSE" | grep "HTTP_STATUS" | cut -d: -f2)
if [ "$HTTP_STATUS" == "400" ]; then
    echo "✓ Duplicate email validation working correctly"
else
    echo "✗ Duplicate email validation failed (Expected 400, got $HTTP_STATUS)"
fi
echo ""

# Test 6: Validation - Invalid Email
echo "6. Testing invalid email validation..."
INVALID_EMAIL_RESPONSE=$(curl -s -w "\nHTTP_STATUS:%{http_code}" -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"invalid-email"}')
HTTP_STATUS=$(echo "$INVALID_EMAIL_RESPONSE" | grep "HTTP_STATUS" | cut -d: -f2)
if [ "$HTTP_STATUS" == "400" ] || [ "$HTTP_STATUS" == "422" ]; then
    echo "✓ Invalid email validation working correctly"
else
    echo "✗ Invalid email validation failed (Expected 400/422, got $HTTP_STATUS)"
fi
echo ""

# Test 7: Delete
echo "7. Deleting profile..."
DELETE_STATUS=$(curl -s -w "\nHTTP_STATUS:%{http_code}" -X DELETE $BASE_URL/$ID | grep "HTTP_STATUS" | cut -d: -f2)
if [ "$DELETE_STATUS" == "204" ]; then
    echo "✓ Profile deleted successfully"
else
    echo "✗ Delete failed (Expected 204, got $DELETE_STATUS)"
fi
echo ""

# Test 8: Verify Deletion
echo "8. Verifying deletion..."
GET_DELETED=$(curl -s -w "\nHTTP_STATUS:%{http_code}" $BASE_URL/$ID | grep "HTTP_STATUS" | cut -d: -f2)
if [ "$GET_DELETED" == "404" ]; then
    echo "✓ Profile deletion verified (404 as expected)"
else
    echo "✗ Profile still exists (Expected 404, got $GET_DELETED)"
fi
echo ""

echo "=== Tests Complete ==="

