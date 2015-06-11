import scala.swing._
import event._
import java.awt.{Color}

class PasswordWindow extends MainFrame {
	// Apply any window properties we require.
	peer.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
	peer.setLocation(new Point(50, 50))

	// Define the components used in this window.
	var field: PasswordField = new PasswordField {}
	var button: Button = new Button { text = "Enter" }
	var label: Label = new Label {}

	// Set appropriate sizes for the components.
	field.preferredSize = new Dimension(200, 40)
	button.preferredSize = new Dimension(100, 40)
	label.preferredSize = new Dimension(200, 40)

	// Set background colour of button.
	button.background = Color.decode("#2196F3")

	// Set window title.
	title = "Enter Password"

	// Request updates from the button i.e. button clicks.
	listenTo(button)

	// Handle the appropriate button clicks.
	reactions += {
		case ButtonClicked(b) => {
			var pass: String = field.password.mkString("")
			if (Tools.hash(pass) == Tools.getHashedPassword()){ 
				Diary.PASS = pass
				var window: DiaryWindow = new DiaryWindow
				window.open()
				this.close()
			} else label.text = "Incorrect Password"
		}
	}

	// Finally set the content of the panel.
	contents = new FlowPanel {
		contents += field
		contents += button
		contents += label
	}
}