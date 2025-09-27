# Accessibility Testing Guide for Aeterna

This guide provides comprehensive instructions for testing the accessibility features of the Aeterna YouTube Music client.

## Prerequisites

- Android device or emulator with API level 24+
- TalkBack service enabled
- Switch Access (if testing switch navigation)
- Voice Access (if testing voice commands)
- Testing device with different screen readers

## Automated Accessibility Tests

### Running Automated Tests

```bash
# Run all accessibility tests
./scripts/test.sh --accessibility-only

# Run specific accessibility test suites
./gradlew :core:ui:testDebugUnitTest --tests "*Accessibility*"
./gradlew :app:connectedDebugAndroidTest --tests "*Accessibility*"
```

### Test Coverage Areas

1. **Contrast Ratio Testing**
   - High contrast mode validation
   - Color accessibility compliance
   - WCAG 2.1 AA standards

2. **Semantic Testing**
   - Content descriptions
   - Role assignments
   - Navigation order

3. **Touch Target Testing**
   - Minimum 48dp touch targets
   - Gesture alternatives
   - Voice control compatibility

## Manual Accessibility Testing

### 1. TalkBack Testing

#### Setup
1. Enable TalkBack: Settings > Accessibility > TalkBack
2. Learn basic TalkBack gestures:
   - Swipe right: Next item
   - Swipe left: Previous item
   - Double tap: Activate
   - Two-finger swipe: Scroll

#### Test Scenarios

**Home Screen Navigation**
- [ ] Welcome message is announced correctly
- [ ] Quick Picks section is properly labeled
- [ ] Song cards have complete descriptions (title, artist, duration)
- [ ] Navigation order is logical

**Music Player Testing**
- [ ] Play/pause button states are announced
- [ ] Skip buttons have clear descriptions
- [ ] Seek bar position and duration are spoken
- [ ] Volume controls announce level changes
- [ ] Album artwork has descriptive alt text

**Search Interface**
- [ ] Search field is properly labeled
- [ ] Search suggestions are navigable
- [ ] Filter options are clearly described
- [ ] Results are announced with count

**Settings Navigation**
- [ ] All settings categories are labeled
- [ ] Toggle states are announced
- [ ] Accessibility settings are functional
- [ ] Navigation hierarchy is clear

### 2. High Contrast Mode Testing

#### Test Cases
- [ ] Enable high contrast mode in settings
- [ ] Verify enhanced color contrast (7:1 ratio)
- [ ] Check elevated shadows and borders
- [ ] Ensure text remains readable
- [ ] Validate icon visibility improvements

#### Validation Points
- [ ] Background/foreground contrast meets standards
- [ ] Interactive elements are easily distinguishable
- [ ] Focus indicators are clearly visible
- [ ] Status indicators use more than color alone

### 3. Reduce Motion Testing

#### Test Cases
- [ ] Enable reduce motion in accessibility settings
- [ ] Verify animations are disabled/reduced
- [ ] Check that functionality remains intact
- [ ] Ensure no auto-playing content

#### Validation Points
- [ ] Page transitions are instantaneous or minimal
- [ ] Loading indicators are static or subtle
- [ ] No parallax effects or excessive motion
- [ ] User-initiated animations only

### 4. Large Text Testing

#### Test Cases
- [ ] Enable large text in system settings
- [ ] Verify app respects system font scaling
- [ ] Check layout adaptation to larger text
- [ ] Ensure no text is cut off

#### Validation Points
- [ ] Text scales up to 200% without issues
- [ ] Buttons remain properly sized
- [ ] Touch targets maintain 48dp minimum
- [ ] Horizontal scrolling is avoided

### 5. Voice Control Testing

#### Setup
1. Enable Voice Access: Settings > Accessibility > Voice Access
2. Learn basic voice commands:
   - "Open [app name]"
   - "Tap [element]"
   - "Scroll down"
   - "Go back"

#### Test Scenarios
- [ ] Navigation commands work correctly
- [ ] Music control via voice ("Play", "Pause", "Next")
- [ ] Search queries can be spoken
- [ ] Settings can be modified by voice

### 6. Switch Access Testing

#### Setup
1. Connect external switch or use volume buttons
2. Enable Switch Access: Settings > Accessibility > Switch Access
3. Configure scanning method (linear or group)

#### Test Cases
- [ ] All interactive elements are reachable
- [ ] Scanning order is logical
- [ ] Actions can be triggered via switch
- [ ] Complex gestures have switch alternatives

## Performance Testing with Accessibility

### TalkBack Performance
- [ ] No significant lag when TalkBack is enabled
- [ ] Smooth navigation between elements
- [ ] Quick response to user input
- [ ] Minimal battery impact

### Memory Usage
- [ ] Accessibility services don't cause memory leaks
- [ ] App remains responsive with multiple a11y services
- [ ] No crashes with accessibility enabled

## Testing Tools and Resources

### Automated Testing Tools
- **Accessibility Scanner**: Google's accessibility testing app
- **Espresso Accessibility Checks**: Automated UI testing
- **Axe for Android**: Accessibility testing library

### Manual Testing Tools
- **TalkBack**: Primary screen reader for Android
- **Voice Access**: Voice control for Android
- **Switch Access**: Switch navigation for Android
- **High Contrast Mode**: System-level contrast enhancement

### Testing Commands

```bash
# Install accessibility scanner
adb install -r accessibility-scanner.apk

# Enable TalkBack via ADB
adb shell settings put secure enabled_accessibility_services com.google.android.marvin.talkback/.TalkBackService

# Take accessibility snapshot
adb shell uiautomator dump --compressed

# Check for accessibility issues
./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.aeterna.test.AccessibilityTestSuite
```

## Testing Checklist

### Pre-Testing Setup
- [ ] Device/emulator configured with accessibility services
- [ ] TalkBack tutorial completed
- [ ] Voice Access calibrated
- [ ] Switch Access configured (if applicable)

### Core Feature Testing
- [ ] Navigation and menu system
- [ ] Music playback controls
- [ ] Search and discovery
- [ ] Library management
- [ ] Settings and preferences
- [ ] Offline functionality

### Accessibility Feature Testing
- [ ] High contrast mode
- [ ] Reduce motion preferences
- [ ] Large text support
- [ ] Voice control compatibility
- [ ] Switch navigation support

### Regression Testing
- [ ] No accessibility regressions in updates
- [ ] New features include accessibility support
- [ ] Performance maintained with a11y services

## Common Issues and Solutions

### Issue: TalkBack not announcing content
**Solution**: Check content descriptions and semantic roles

### Issue: Elements not focusable
**Solution**: Verify touch targets are minimum 48dp and have proper semantics

### Issue: Poor contrast in high contrast mode
**Solution**: Use AccessibilityUtils.getAccessibleColor()

### Issue: Animations cause motion sensitivity
**Solution**: Respect reduce motion preference

### Issue: Voice commands not recognized
**Solution**: Ensure proper content descriptions and action labels

## Reporting Accessibility Issues

When reporting accessibility issues, include:
1. Device and OS version
2. Accessibility service(s) enabled
3. Steps to reproduce
4. Expected vs actual behavior
5. Screen recording or audio description
6. Impact on user experience

## Compliance Standards

Aeterna aims to meet:
- **WCAG 2.1 AA**: International accessibility guidelines
- **Section 508**: US federal accessibility requirements
- **Android Accessibility Guidelines**: Platform-specific requirements
- **Material Design Accessibility**: Google's design guidelines

## Resources

- [Android Accessibility Developer Guide](https://developer.android.com/guide/topics/ui/accessibility)
- [WCAG 2.1 Guidelines](https://www.w3.org/WAI/WCAG21/quickref/)
- [TalkBack User Guide](https://support.google.com/accessibility/android/answer/6283677)
- [Material Design Accessibility](https://material.io/design/usability/accessibility.html)