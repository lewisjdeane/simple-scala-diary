import scala.swing._
import event._
import java.io.{File, PrintWriter}
import java.awt.{Color}
import scala.swing.BorderPanel.Position._

class SetPasswordWindow extends MainFrame {
	// Apply any window properties we require.
	peer.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
	peer.setLocation(new Point(50, 50))

	// Define all the components we will need.
	var field: PasswordField = new PasswordField {}
	var fieldConfirm: PasswordField = new PasswordField {}
	var button: Button = new Button { text = "Set" }
	var label: Label = new Label {}
	var passwordLabel = new Label { text = "Password"; horizontalAlignment = Alignment.Right }
	var confirmPasswordLabel = new Label { text = "Confirm Password"; horizontalAlignment = Alignment.Right }

	// Set the sizes of the components.
	field.preferredSize = new Dimension(200, 40)
	fieldConfirm.preferredSize = new Dimension(200, 40)
	button.preferredSize = new Dimension(100, 40)
	label.preferredSize = new Dimension(300, 40)
	passwordLabel.preferredSize = new Dimension(150, 40)
	confirmPasswordLabel.preferredSize = new Dimension(150, 40)

	// Set background of button.
	button.background = Color.decode("#2196F3")

	// Set window title.
	title = "Set Password"

	// Request updates to events on the button.
	listenTo(button)

	// Handle these events.
	reactions += {
		case ButtonClicked(b) => {
			var pass: String = field.password.mkString("")
			var pass2: String = fieldConfirm.password.mkString("")

			if (pass == pass2) {
				if (matchesConditions(pass)) {
					setPassword(pass)
					Diary.PASS = pass
					var window: DiaryWindow = new DiaryWindow
					window.open()
					this.close()
				} else label.text = "Password should be between 8 and 16 characters"
			} else label.text = "Passwords don't match"
		}
	}

	// Set the window contents.
	contents = new FlowPanel {
		contents += new BorderPanel {
			layout(passwordLabel) = North
			layout(confirmPasswordLabel) = Center
		}
		contents += new BorderPanel {
			layout(field) = North
			layout(fieldConfirm) = Center
		}
		contents += new BorderPanel {
			layout(button) = North
			layout(label) = Center
		}
	}

	/*
		Sets the password by writing a hash of the password to a text file.
	*/
	private def setPassword(pass: String): Unit = {
		val writer = new PrintWriter(new File("PASSWORD.txt"))
		var hashedPass: String = Tools.hash(pass)

	      	writer.write(hashedPass)
	      	writer.close()

	      	Tools.clearEntryDirectory()
	      	Tools.setupEntryDirectory()
	}

	/*
		Returns a boolean of whether or not the password entered has length between 8 and 16.
	*/
	private def matchesConditions(pass: String): Boolean = {
		if (pass.length > 7 && pass.length < 17) true
		else false
	}
}