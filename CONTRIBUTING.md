# Contributing to Aeterna

Thank you for your interest in contributing to Aeterna! This document provides guidelines and information for contributors.

## 🌟 Ways to Contribute

### 🐛 Bug Reports
- Search existing issues before creating new ones
- Use the bug report template
- Include device information, Android version, and steps to reproduce
- Add screenshots or recordings when helpful

### ✨ Feature Requests
- Check if the feature has already been requested
- Use the feature request template
- Explain the use case and expected behavior
- Consider accessibility implications

### 🔧 Code Contributions
- Fork the repository and create a feature branch
- Follow the coding standards and architecture patterns
- Add tests for new functionality
- Update documentation as needed
- Ensure accessibility compliance

### ♿ Accessibility Improvements
- Test with TalkBack and other assistive technologies
- Follow WCAG 2.1 AA guidelines
- Consider users with different abilities
- Document accessibility features

### 🌍 Localization
- Help translate the app to your language
- Follow localization guidelines
- Consider cultural context and accessibility

## 🛠️ Development Setup

### Prerequisites
- Android Studio 2023.3.1+
- JDK 17+
- Git
- Android SDK with API 34

### Getting Started
```bash
# Fork and clone the repository
git clone https://github.com/yourusername/aeterna.git
cd aeterna

# Set up development environment
./dev_setup.sh

# Build and test
./gradlew assembleDebug
./scripts/test.sh
```

## 📝 Coding Standards

### Kotlin Style
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add KDoc comments for public APIs
- Prefer immutable data structures

### Architecture Guidelines
- Follow Clean Architecture principles
- Use MVVM pattern for UI components
- Implement proper separation of concerns
- Write testable code with dependency injection

### Accessibility Requirements
- All UI elements must have content descriptions
- Support TalkBack navigation
- Ensure minimum 48dp touch targets
- Test with accessibility services enabled
- Follow Material Design accessibility guidelines

### Testing Requirements
- Write unit tests for business logic
- Add integration tests for critical flows
- Include accessibility tests
- Maintain test coverage above 80%

## 🔄 Pull Request Process

### Before Submitting
1. **Test thoroughly** on different devices and Android versions
2. **Run all tests** and ensure they pass
3. **Check accessibility** with TalkBack enabled
4. **Update documentation** if needed
5. **Follow commit message conventions**

### PR Requirements
- Clear description of changes
- Link to related issues
- Screenshots for UI changes
- Accessibility testing confirmation
- Code review checklist completed

### Review Process
1. **Automated checks** must pass (CI/CD)
2. **Code review** by maintainers
3. **Accessibility review** for UI changes
4. **Testing** on multiple devices
5. **Documentation review** if applicable

## 🧪 Testing Guidelines

### Unit Tests
```bash
# Run unit tests
./gradlew testDebugUnitTest

# With coverage
./scripts/test.sh --unit-only --with-coverage
```

### Integration Tests
```bash
# Run on connected device/emulator
./gradlew connectedDebugAndroidTest

# Accessibility tests specifically
./scripts/test.sh --accessibility-only
```

### Manual Testing
- Test core functionality end-to-end
- Verify accessibility with TalkBack
- Test on different screen sizes
- Check performance and battery usage

## 📚 Documentation

### Code Documentation
- Add KDoc comments for public APIs
- Document complex algorithms
- Explain accessibility features
- Include usage examples

### User Documentation
- Update README for new features
- Add to accessibility guide if relevant
- Include screenshots and examples
- Keep installation instructions current

## 🎯 Issue Labels

### Priority
- `priority:critical` - Security issues, crashes
- `priority:high` - Important bugs, accessibility issues
- `priority:medium` - Feature requests, improvements
- `priority:low` - Nice-to-have features

### Type
- `bug` - Something isn't working
- `enhancement` - New feature or request
- `accessibility` - Accessibility-related
- `documentation` - Documentation improvements
- `good first issue` - Good for newcomers

### Status
- `help wanted` - Extra attention needed
- `blocked` - Waiting for dependency
- `wontfix` - Won't be implemented
- `duplicate` - Duplicate issue

## 🔒 Security

### Reporting Security Issues
- **DO NOT** open public issues for security vulnerabilities
- Email security@aeterna.app with details
- Include steps to reproduce and potential impact
- Allow time for response before public disclosure

### Security Guidelines
- Never commit API keys or sensitive data
- Use secure coding practices
- Follow Android security best practices
- Keep dependencies updated

## 📄 License

By contributing to Aeterna, you agree that your contributions will be licensed under the GNU General Public License v3.0.

### Why GPL v3?
- Ensures the project remains open source
- Protects user freedom and accessibility
- Promotes community-driven development
- Prevents proprietary forks

## 🤝 Community Guidelines

### Code of Conduct
- Be respectful and inclusive
- Welcome newcomers and different perspectives
- Focus on constructive feedback
- Help create a positive environment

### Communication
- Use clear, descriptive titles for issues and PRs
- Be patient with review processes
- Ask for help when needed
- Share knowledge and learn from others

## 🏆 Recognition

### Contributors
All contributors are recognized in:
- README.md contributors section
- Release notes for significant contributions
- Annual contributor highlights

### Types of Recognition
- **Code contributors** - Implementation and bug fixes
- **Accessibility advocates** - Accessibility improvements
- **Translators** - Localization efforts
- **Testers** - Quality assurance and bug reports
- **Documenters** - Documentation improvements

## 📞 Getting Help

### Development Questions
- Check existing documentation first
- Search closed issues for solutions
- Ask in GitHub Discussions
- Join our development chat

### Resources
- [Android Development Guide](https://developer.android.com/guide)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Accessibility Guidelines](https://developer.android.com/guide/topics/ui/accessibility)
- [Material Design](https://material.io/design)

## 🎉 Thank You!

Your contributions make Aeterna better for everyone. Whether you're fixing bugs, adding features, improving accessibility, or helping with documentation, every contribution matters.

Together, we're building a more accessible and inclusive music experience for all users! 🎵♿✨

---

*For questions about contributing, feel free to reach out via GitHub Discussions or email contribute@aeterna.app*