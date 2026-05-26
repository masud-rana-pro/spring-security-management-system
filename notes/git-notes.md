# Git Workflow Notes - Secure Auth Lab

This document explains the Git workflow used in this project with feature branches and solo developer setup.

---

## Branch Strategy

```
main          (stable production code)
  |
  +-- dev     (integration branch, all features merge here)
       |
       +-- feature/xxx    (feature branches, one per feature)
       +-- fix/xxx        (bug fix branches)
```

### Branch Descriptions

| Branch | Purpose |
|--------|---------|
| `main` | Production-ready stable code. Only merged from `dev` after complete testing |
| `dev` | Integration branch where all features are combined and tested |
| `feature/xxx` | One branch per feature (e.g., `feature/login-api`, `feature/jwt-auth`) |
| `fix/xxx` | One branch per bug fix (e.g., `fix/email-validation`) |

---

## Daily Workflow (Step by Step)

### Step 1: Start a New Feature

```bash
# Go to project root
cd C:\Users\masud\projects-for-github\security-management-system

# Switch to dev and get latest code
git checkout dev
git pull origin dev

# Create a new feature branch
git checkout -b feature/your-feature-name
```

### Step 2: Work on Code Changes

- AI assistant writes the code
- You can check file changes anytime:
  ```bash
  git status
  git diff
  ```

### Step 3: Commit and Push Feature Branch

```bash
# Check what files changed
git status

# Add all changes
git add .

# Commit with descriptive message
git commit -m "Description of what was implemented"

# Push feature branch to GitHub
git push -u origin feature/your-feature-name
```

### Step 4: Merge to Dev Branch

```bash
# Switch back to dev
git checkout dev

# Get latest dev from remote
git pull origin dev

# Merge your feature branch into dev
git merge feature/your-feature-name

# Push updated dev to GitHub
git push origin dev
```

### Step 5: (Optional) Delete Local Feature Branch

```bash
git branch -d feature/your-feature-name
```

---

## Example Commit Messages

| Scenario | Commit Message |
|----------|---------------|
| New feature | `Implement JWT token generation and authentication filter` |
| Bug fix | `Fix validation error in RegisterRequest for email field` |
| Config change | `Add CORS configuration for frontend origin` |
| Refactor | `Refactor AuthService to use custom exceptions` |
| Documentation | `Add educational English comments to existing codebase` |

---

## Common Git Commands Reference

```bash
# Check current branch
git branch

# Check file changes
git status

# See what changed in files
git diff

# See commit history
git log --oneline

# Undo changes in a file (before commit)
git checkout -- filename

# Undo last commit (keep changes)
git reset --soft HEAD~1

# Undo last commit (discard changes)
git reset --hard HEAD~1

# Fetch latest from remote without merging
git fetch origin

# See all branches including remote
git branch -a
```

---

## Conflict Resolution

If you get a merge conflict:

```bash
# Git will show which files have conflicts
git status

# Open the conflicting files and look for:
# <<<<<<< HEAD
# your code
# =======
# incoming code
# >>>>>>> feature/xxx

# Fix the conflicts manually, then:
git add .
git commit -m "Resolve merge conflicts"
```

---

## Solo Developer Tips

Since you are working alone:

1. **Always create feature branches** - Even as solo, branches keep history clean
2. **Commit often** - Small commits are easier to understand and revert
3. **Push after each step** - Backs up your work to GitHub
4. **Merge to dev after testing** - Never merge untested code
5. **Keep dev branch stable** - dev should always compile and run