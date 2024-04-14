# Slime launcher
> _All you need is slime._

![Slime Launcher Logo](docs/assets/slim-logo.jpg)

## Background
>A minimalist launcher that encourage you to use fewer apps on your device.
 
This project is based on [Slim Launcher](https://github.com/sduduzog/slim-launcher).  
If the idea of creating a launcher that limits the number of accessible applications is a good one, I primarily needed a simple launcher onto which I could add elements based on my needs. The limitation to 7 visible applications from the initial project was removed to allow access to all phone applications. However, up to 7 applications can be pinned to the home screen, encouraging the avoidance of launching others.

# Contributions

## Issues
**1.** Create an issue with suggestions for what to change, or do so yourself.
Within the scope of what the app is, features will be added in the next release following friendly discussion.

**2.** Help is needed anywhere you see fit, please do.

## Code reviews/new PRs etc
**3.** I think anyone can contribute, at least, not even writing a single line of code, any open pull request is open for discussion. If you want to submit a new pull request, open an issue for it, 
if it doesn't exist already. 
When I'm working on something big (new features maybe), I'll probably have a PR for it. If you want to help me out, you can create a pull request against that feature branch, I'd appreciate that a lot.
Also I'm working on [v3.0](https://github.com/sduduzog/slim-launcher/pull/98) and I think it'll be cool to collaborate with strangers on it :)

### Running/Writing tests
- Unit tests use robolectric to run tests on the VM and it currently works with [Java11](https://adoptopenjdk.net/releases.html) for me and does not work with anything below that.
 The project though doesn't complain about the jdk version you're using as far as project development is concerned.
 The point of reintroducing tests to the project is to make sure that there's less brittle parts around the app as more and more tests are added.
 When submitting a PR, for now you don't really have to write unit tests or anything but if you can, please do, but existing tests should not be removed just to pass the build once hooked up to the CI.

## [Buy upstream dev a coffee](https://buymeacoff.ee/sduduzog)
He deserves it
