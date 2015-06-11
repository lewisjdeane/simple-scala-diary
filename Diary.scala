import scala.swing._

object Diary {

	// This will be updated once the user signs in, we can then use this password for encrypting and decrypting entries.
	var PASS: String = ""

	def main(args: Array[String]): Unit = {
		// This is run on start up and we handle the following cases.
		if (Tools.passwordExists) {
			var window: PasswordWindow = new PasswordWindow
			window.open()
		} else {
			var window: SetPasswordWindow = new SetPasswordWindow
			window.open()
		}
	}
}