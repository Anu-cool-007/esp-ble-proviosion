## ESP BLE provisioner

Android app to provision esp devices using QR code
This project uses Jetpack Compose UI

This app has 2 screens that uses different methods to read QR code
- Custom QR reader using zxing and CameraX
- ESP provided QR reader

### Todo

- [x] Create a base screen that navigates to Custom and ESP scanner screens
- [x] Create custom QR scanner screen that outputs text
- [ ] Custom scanner should use the text to provision device
- [ ] Implement QR scanner provided by ESP lib in ESP Provision screen
