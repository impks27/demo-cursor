# Cursor vs Copilot -- Evaluation Checklist

## 1. Repository-Level Intelligence Tests

### 1.1 Full Project Understanding

Prompt: \> Explain the full architecture of this repo, list modules,
flows, and dependencies.

**Cursor:** Deep repo-wide understanding\
**Copilot:** Limited to open file context

------------------------------------------------------------------------

### 1.2 Cross-File Consistency

Prompt: \> Find all components using outdated function X and update them
to function Y.

**Cursor:** Semantic search + bulk rewrite\
**Copilot:** Only updates current file

------------------------------------------------------------------------

## 2. Multi-File Editing & Transformations

### 2.1 Multi-File Refactor

Prompt: \> Convert entire project from JavaScript to TypeScript and fix
all imports.

### 2.2 Code Style Standardization

Prompt: \> Rewrite all services to use async/await and unify error
handling.

### 2.3 Framework Migrations

Prompt: \> Migrate all Express routes to NestJS modules.

------------------------------------------------------------------------

## 3. Debugging & Error Fixing Tests

### 3.1 Debugging Large Logs

Prompt: \> Find the error and fix the root cause across the repo.

### 3.2 Fixing Circular Dependencies

Prompt: \> Fix circular import issues and restructure modules.

------------------------------------------------------------------------

## 4. Feature Addition Tests

### 4.1 Full Feature Request

Prompt: \> Add user authentication (JWT). Include routes, DB models,
middleware, and tests.

### 4.2 Add a Full UI Screen

Prompt: \> Add a dashboard screen and wire it to backend API X.

### 4.3 End-to-End CRUD

Prompt: \> Add CRUD for Products end-to-end.

------------------------------------------------------------------------

## 5. Search, Review & Rewrite Tests

### 5.1 Semantic Search

Prompt: \> Find functions that do risky DB writes.

### 5.2 Code Review Quality

Prompt: \> Review this PR and apply suggested fixes.

### 5.3 Security Audit

Prompt: \> Find security vulnerabilities and fix them.

------------------------------------------------------------------------

## 6. Agent Workflow Tests

### 6.1 Multi-Step Reasoning

Prompt: \> Search the repo for slow functions, benchmark them, and
optimize.

### 6.2 Architectural Recommendations

Prompt: \> Refactor this into hexagonal architecture.

------------------------------------------------------------------------

## 7. Productivity & Quality Tests

### 7.1 Edit Review

Prompt: \> Show diffs for everything you changed.

### 7.2 Long-Term Memory of Edits

Compare continuity across prompts.

### 7.3 Iteration Speed

Measure time taken for: - Feature addition - Bug fix - Refactor

------------------------------------------------------------------------

## 8. Final Stress Test -- Real Scenario

Prompt: \> Build a complete user profile feature including DB model,
API, UI form, validations, route updates, and tests.

Measure: - Steps completed\
- Files created/updated\
- End-to-end correctness\
- Build success
