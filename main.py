from kivy.app import App
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.label import Label
from kivy.uix.textinput import TextInput
from kivy.uix.button import Button
import webbrowser

class CookieApp(App):
    def build(self):
        self.title = "FB Cookie Session"
        layout = BoxLayout(orientation='vertical', padding=20, spacing=10)
        
        layout.add_widget(Label(text="Enter FB Cookies", font_size='20sp'))
        
        self.cookie_input = TextInput(hint_text="Paste cookies here...", multiline=True)
        layout.add_widget(self.cookie_input)
        
        btn = Button(text="Open Session", size_hint=(1, 0.2), background_color=(0, 0.5, 1, 1))
        btn.bind(on_press=self.login)
        layout.add_widget(btn)
        
        return layout

    def login(self, instance):
        # هنا يتم فتح الرابط (لاحقاً سنضيف حقن الكوكيز في WebView)
        webbrowser.open("https://m.facebook.com")

if __name__ == "__main__":
    CookieApp().run()
