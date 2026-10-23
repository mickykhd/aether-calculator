---
name: Aether Compute
colors:
  surface: '#111319'
  surface-dim: '#111319'
  surface-bright: '#373940'
  surface-container-lowest: '#0c0e14'
  surface-container-low: '#191b22'
  surface-container: '#1e1f26'
  surface-container-high: '#282a30'
  surface-container-highest: '#33343b'
  on-surface: '#e2e2eb'
  on-surface-variant: '#c7c4d8'
  inverse-surface: '#e2e2eb'
  inverse-on-surface: '#2e3037'
  outline: '#918fa1'
  outline-variant: '#464555'
  surface-tint: '#c3c0ff'
  primary: '#c3c0ff'
  on-primary: '#1d00a5'
  primary-container: '#635bff'
  on-primary-container: '#fefaff'
  inverse-primary: '#4c42e9'
  secondary: '#ffb3b0'
  on-secondary: '#68000f'
  secondary-container: '#901822'
  on-secondary-container: '#ff9e9b'
  tertiary: '#d0bcff'
  on-tertiary: '#3c0091'
  tertiary-container: '#8354ee'
  on-tertiary-container: '#fffafd'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#e2dfff'
  primary-fixed-dim: '#c3c0ff'
  on-primary-fixed: '#0f0069'
  on-primary-fixed-variant: '#321ed2'
  secondary-fixed: '#ffdad8'
  secondary-fixed-dim: '#ffb3b0'
  on-secondary-fixed: '#410006'
  on-secondary-fixed-variant: '#8c1520'
  tertiary-fixed: '#e9ddff'
  tertiary-fixed-dim: '#d0bcff'
  on-tertiary-fixed: '#23005c'
  on-tertiary-fixed-variant: '#5516be'
  background: '#111319'
  on-background: '#e2e2eb'
  surface-variant: '#33343b'
typography:
  headline-display-mobile:
    fontFamily: Space Mono
    fontSize: 48px
    fontWeight: '700'
    lineHeight: 56px
    letterSpacing: -0.04em
  headline-display-compressed:
    fontFamily: Space Mono
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.03em
  headline-expression:
    fontFamily: Space Mono
    fontSize: 22px
    fontWeight: '400'
    lineHeight: 28px
    letterSpacing: -0.01em
  headline-keypad-primary:
    fontFamily: Space Grotesk
    fontSize: 28px
    fontWeight: '500'
    lineHeight: 32px
    letterSpacing: 0em
  headline-keypad-operator:
    fontFamily: Inter
    fontSize: 30px
    fontWeight: '400'
    lineHeight: 32px
    letterSpacing: 0em
  body-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
    letterSpacing: 0em
  body-sm:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
    letterSpacing: 0.01em
  label-keypad-secondary:
    fontFamily: Space Grotesk
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.04em
  label-mono-tag:
    fontFamily: Space Mono
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 14px
    letterSpacing: 0.05em
rounded:
  sm: 0.5rem
  DEFAULT: 1rem
  md: 1.5rem
  lg: 2rem
  xl: 3rem
  full: 9999px
spacing:
  gutter: 0.75rem
  margin: 1rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 0.75rem
  space-lg: 1.25rem
  space-xl: 2rem
---

## Brand & Style

The design system embodies the confluence of precision engineering and fluid, tactile interaction on modern Android devices. Designed specifically for next-generation mobile calculation, the aesthetic draws upon Google Pixel's expressive Material You sensibilities while introducing a deep, architectural twilight palette and neon optical accents.

The target audience spans students, developers, financial analysts, and everyday mobile power users who value immediate visual clarity, ergonomic touch targets, and subtle visual luxury. The interface evokes a sense of computational mastery, hyper-focus, and effortless fluidity. 

### Design Movement & Visual Style
- **Expressive Tactile Modernism:** A hybrid of deep charcoal slate tonal surfaces, dynamic Material You pill and circular geometries, and luminescent electric-indigo, violet, and fiery coral focal points.
- **Micro-Glassmorphic Accents:** Secondary panels, memory registers, and slide-over history sheets utilize soft backdrop-blur surfaces (`rgba(255, 255, 255, 0.04)` over deep slate) to ground floating elements without visual clutter.
- **Haptic & Dynamic Spring Motion:** Every key strike compresses slightly under the thumb with an immediate radial bloom, delivering instantaneous confidence through visual inertia.

## Colors

The palette establishes an ultra-low eye-strain twilight canvas punctuated by high-luminance neon indicators:

- **Primary (`#635BFF` - Electric Indigo):** Reserved for primary evaluation actions (equals sign, active state toggles, and conversion locks). It vibrates with energy without bleeding across dark boundaries.
- **Secondary (`#FF6B6B` - Solar Coral):** Applied strictly to arithmetic operators (`÷`, `×`, `−`, `+`) and destructive controls (`AC`, `C`, `DEL`). This brings instant visual triage between input data and operational logic.
- **Tertiary (`#8B5CF6` - Vivid Violet):** Assigned to scientific sub-functions, trigonometric selectors, radial/degree toggles, and memory registers (`MC`, `MR`, `M+`).
- **Neutral Surface Palette:**
  - `Surface Canvas`: `#0F1117` (Deep Obsidian Slate)
  - `Surface Keypad Plate`: `#161922` (Subtle elevated charcoal backdrop)
  - `Key Normal Surface`: `#1F2330` (Tactile slate-tinted key resting state)
  - `Key Number Surface`: `#252A3A` (Slightly elevated key for rapid indexing)
  - `On-Surface Primary`: `#F1F3F9` (Pristine, non-glare off-white for digits)
  - `On-Surface Secondary`: `#9AA0B4` (Muted cool gray for sub-labels and formula tracking)
  - `Active Pressed Tint`: Dynamic `rgba(255, 255, 255, 0.12)` overlay on interactive elements.

## Typography

The type architecture separates calculation readouts from interactive key commands:

- **Primary Readout (`Space Mono`):** Digits and computational expressions require strict monospaced alignment to prevent horizontal jitter during rapid continuous typing and scrolling calculations. Display size automatically transitions from `headline-display-mobile` (48px) down to `headline-display-compressed` (32px) when digit length exceeds 9 characters.
- **Key Caps (`Space Grotesk` & `Inter`):** Numbers use `Space Grotesk` for its balanced, modern geometric proportions, while algebraic glyphs (`+`, `−`, `×`, `÷`) utilize `Inter` for standard typographical baseline parity and optical symmetry.
- **Secondary Modifiers (`Space Mono`):** Scientific secondary functions (`sin⁻¹`, `ln`, `RAD`, `DEG`) utilize uppercase fixed-width tracking to preserve structural rhythm across condensed keypad tiers.

## Layout & Spacing

The layout is built specifically for thumb-driven one-handed reach on mobile displays:

### Spatial Division
- **Display Viewport (Top 35-40% of viewport):** A vertically stacked, bottom-aligned reading canvas with fluid auto-scrolling horizontal overflow for prolonged arithmetic chains. Padding maintains `space-lg` from the device edge.
- **Keypad Matrix (Bottom 60-65% of viewport):** Standard 4-column layout for basic calculations expanding to a 5-column sliding grid for advanced scientific computation.

### Grid Rhythm & Touch Ergonomics
- Column gaps and row gaps are pinned to `gutter` (12px / 0.75rem) to ensure clear visual separation while preventing mis-taps.
- Key buttons maintain an optical aspect ratio of 1:1 (circular) or 1.2:1 (soft pill) based on grid distribution, with a strict minimum touch target of 64px × 64px.
- Screen margins use `margin` (16px / 1rem) at device edges, expanding to safe areas along Android home navigation gestures.

## Elevation & Depth

Visual depth is conveyed through tonal layering, inner edge glows, and luminous ambient diffusion:

### Tonal Hierarchy
- **Level 0 (Root Canvas):** `#0F1117` base.
- **Level 1 (Keypad Cradle):** `#161922` surface background with a 1px border gradient of `rgba(255, 255, 255, 0.05)` to define boundaries against the canvas.
- **Level 2 (Standard Keys):** Raised slate keys `#1F2330` sporting a top inset highlight (`inset 0 1px 0 rgba(255, 255, 255, 0.08)`) that simulates physical injection-molded hardware.
- **Level 3 (Floating Actions & Sheets):** History drawer and currency/unit picker sheets apply background blur (16px) with 85% opacity `#161922` and a diffuse ambient drop shadow (`0 12px 32px rgba(0, 0, 0, 0.45)`).

### Luminous Operator Glows
Primary evaluate (`=`) and active operator toggles project an ambient colored shadow matching their tint:
- Indigo Evaluation Button: `0 6px 20px rgba(99, 91, 255, 0.35)`
- Coral Active Operators: `0 4px 16px rgba(255, 107, 107, 0.30)`

## Shapes

The design system adopts a **Pill-shaped (Level 3)** design language inspired by Google Material You's expressive form factors:

- **Keypad Buttons:** Full pill geometry (`border-radius: 9999px`) on elongated keys (such as `0` or dynamic `=` action keys) and perfect 1:1 circular shapes for numerical inputs and operator keys.
- **Expression Display Card:** Soft-curved container with `rounded-xl` (2rem / 32px) corner radii.
- **Interactive Sheets & Segmented Controls:** Encapsulated in complete stadium pills (`9999px`) to facilitate smooth swiping gestures across screen boundaries.
- **Touch Indicators:** Material ripples propagate radially within the bounds of each key's pill curvature.

## Components

### 1. Keypad Buttons
- **Numeric Keys (0-9, .):** Circular or stadium pill shape. Background `#252A3A`, label text `#F1F3F9` in `Space Grotesk Medium`. Surface reflects a subtle top-lit bevel via 1px inner highlight. Pressed state scales down to `0.94` with a quick 120ms spring.
- **Arithmetic Operators (÷, ×, −, +):** Pill/Circular button. Background `rgba(255, 107, 107, 0.12)`, label text `#FF6B6B`. When active/selected, background transitions into solid `#FF6B6B` with white text and coral ambient glow.
- **Destructive Keys (AC, C, DEL):** Text color `#FF6B6B`, background `#1F2330`. Long-press on `DEL` triggers progressive backspace acceleration.
- **Evaluation Action Button (=):** Stadium pill spanning column width or dedicated primary node. Background solid `#635BFF`, label text `#FFFFFF` in `Space Grotesk Bold`. In active state, casts an electric-indigo halo.
- **Scientific Keys (sin, cos, tan, rad, √):** Muted slate background `#181B26` with `#8B5CF6` typography in `label-keypad-secondary`. Compact vertical height with 12px vertical padding.

### 2. Readout & Expression Display
- **Live Expression String:** Right-aligned, `headline-expression` in `#9AA0B4`. Allows inline scrolling and cursor insertion.
- **Primary Numerical Readout:** Right-aligned, `Space Mono Bold` in `#F1F3F9`. Dynamic auto-scaling font size based on input length.
- **Rad/Deg Indicator Pill:** Compact `label-mono-tag` chip positioned top-left of the display viewport (`rgba(139, 92, 246, 0.15)` background with `#8B5CF6` text).

### 3. History Drawer / Tape
- Modal bottom-sheet accessible via down-swipe on readout or top-bar history icon.
- Surface uses frosted glass (`#161922` at 90% opacity, 20px blur) with an upper pill grab-handle (`4px × 36px`, `#3A3F50`).
- Individual history items render previous expressions in `#9AA0B4` and answers in `#F1F3F9`, selectable on tap to repopulate active calculation registers.

### 4. Mode Switcher (Basic / Scientific / Unit Converter)
- Segmented control enclosed in a continuous stadium pill container (`#161922`).
- Active segment glides seamlessly between states using a floating `#252A3A` pill layer with tactile haptic feedback on handoff. Text colors transition from `#9AA0B4` to high-contrast `#F1F3F9`.