import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        InitKoinKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { onOpenURL in
                    ExternalUriHandler.shared.onNewUri(uri: uri.absoluteString)
            }
        }
    }
}