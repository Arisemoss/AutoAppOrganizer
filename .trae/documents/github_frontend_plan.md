# Plan: GitHub Repository Management + Web Landing Page for Auto App Organizer

## Summary

Two parallel objectives for the **桌面整理 (Auto App Organizer)** Android app:
1. **GitHub Plugin** — Set up and manage the GitHub repository (check repo status, configure issues/labels, verify CI workflow, prepare for releases).
2. **Frontend Design Plugin** — Create a polished, production-grade web landing page (static HTML/CSS/JS) to serve as the project's public face, deployable via GitHub Pages.

---

## Current State Analysis

### Project Overview
- **App**: Auto App Organizer (桌面整理) — Android app using AccessibilityService to auto-categorize home screen apps into folders
- **Package**: `com.autoapporganizer`, version 1.00, targetSdk 34, minSdk 24
- **Language**: Kotlin, build with Gradle 8.2.2 / Kotlin 1.9.22
- **CI**: `.github/workflows/android-build.yml` — builds debug+release APK, runs unit tests, uploads artifacts on push to main
- **Features**: Auto-categorize, widget protection, local category dictionary (12 categories), backup/undo, diagnostic logging

### Key Files
| File | Purpose |
|------|---------|
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | Core accessibility service |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | Main UI with organize button, diagnostics, permissions |
| `app/src/main/assets/categories.json` | 12-category app classification dictionary |
| `app/build.gradle.kts` | App-level build config (version 1.00) |
| `.github/workflows/android-build.yml` | CI: build + test + upload APK artifacts |

### Missing / Needed
- No GitHub Pages site or web presence
- No existing `gh-pages` branch or `docs/` folder for static site
- README is Chinese-only, no English version
- No GitHub labels, issue templates, or release tags visible in local repo

---

## Proposed Changes

### Part 1: GitHub Repository Management (via `trae-remote-official:github` plugin)

#### Step 1.1 — Check repository status
- Use `get_me` to verify GitHub authentication
- Use `search_repositories` or direct query to find the existing remote repo
- Use `list_branches`, `list_tags`, `list_releases` to audit current state

#### Step 1.2 — Configure repository
- **Labels**: Create standardized labels (e.g., `bug`, `feature`, `enhancement`, `android`, `ui`, `accessibility`, `category-dict`, `good first issue`)
- **Issue template**: Add `.github/ISSUE_TEMPLATE/bug_report.yml` and `feature_request.yml` for structured issue reporting
- **Branch protection** (if permissions allow): Require PR checks on `main`

#### Step 1.3 — Verify & enhance CI workflow
- The existing `android-build.yml` is solid (build + test + upload)
- Add a **GitHub Pages deploy workflow** (`.github/workflows/deploy-pages.yml`) that publishes the landing page from `docs/` on push to `main`

#### Step 1.4 — Create initial release
- Use `list_releases` / `get_latest_release` to check for existing releases
- If none, create a `v1.0.0` draft release with the current APK artifacts

---

### Part 2: Web Landing Page (via `trae-remote-official:frontend-design` skill)

#### Step 2.1 — Design and build the landing page
Invoke the `frontend-design` skill to create a static landing page at `docs/index.html` (plus CSS/JS inline or in `docs/`).

**Design requirements:**
- **App name**: 桌面整理 / Auto App Organizer
- **Tagline**: "一键自动整理你的 Android 桌面" / "Auto-organize your Android home screen with one tap"
- **Visual style**: Clean, modern, Material-inspired; slate-blue + warm-stone palette (matching the Android app's design direction)
- **Sections**:
  1. **Hero** — App name, tagline, hero illustration (phone mockup with organized icons), download/GitHub CTA buttons
  2. **Features** — 6 feature cards: Auto-categorize, Widget protection, Smart dictionary, Backup & Undo, Fast & Simple, Privacy-first
  3. **How it works** — 3-step guide: Install → Enable accessibility → Tap to organize
  4. **Categories showcase** — Grid of 12 category pills (社交, 购物, 视频, 音乐, etc.)
  5. **GitHub section** — Link to repo, star count badge, contribution CTA
  6. **Footer** — License, author, version
- **Responsive**: Mobile-first, works on all screen sizes
- **Bilingual**: Primary Chinese with English subtitles
- **No frameworks**: Pure HTML + CSS + JS (for GitHub Pages simplicity)
- **Dark mode**: Support `prefers-color-scheme: dark`

#### Step 2.2 — Place files for GitHub Pages
- All files go in `docs/` directory (GitHub Pages can serve from `docs/` on `main` branch)
- Structure:
  ```
  docs/
  ├── index.html      # Main landing page
  ├── style.css       # Styles (or inline in HTML)
  └── script.js       # Minimal JS (or inline)
  ```

#### Step 2.3 — Configure GitHub Pages
- Add a **deploy-pages workflow** (`.github/workflows/deploy-pages.yml`) using `actions/deploy-pages@v4`
- Or configure the repo to serve from `docs/` on `main` branch (simpler, no workflow needed)

---

## Implementation Order

1. **GitHub auth & repo check** — Verify plugin access, find/create remote repo
2. **Create landing page** — Use `frontend-design` skill to generate `docs/index.html`
3. **Add GitHub Pages config** — Create deploy workflow or configure `docs/` source
4. **Configure repo** — Labels, issue templates, branch protection
5. **Create release** — Tag v1.0.0 and draft release with APK

---

## Assumptions & Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Pages source | `docs/` folder on `main` | Simplest setup, no extra branch needed |
| Landing page tech | Static HTML/CSS/JS | No build step needed, GitHub Pages native |
| Language | Bilingual (CN primary, EN secondary) | Matches README style and target audience |
| CI for Pages | `actions/deploy-pages@v4` | Standard, works with `docs/` source |
| Version for release | v1.0.0 | Matches `versionName = "1.00"` in build config |
| Issue templates | YAML structured templates | Better than markdown for structured data |

---

## Verification Steps

1. **Landing page**: Open `docs/index.html` locally in browser — verify layout, responsiveness, dark mode
2. **GitHub Pages**: After deploy, visit `https://{owner}.github.io/{repo}/` — confirm page loads
3. **CI**: Push to main, check Actions tab — both `android-build.yml` and `deploy-pages.yml` pass
4. **Labels**: Check repo Issues → Labels — confirm custom labels exist
5. **Release**: Check repo Releases — confirm v1.0.0 draft exists with APK artifact
