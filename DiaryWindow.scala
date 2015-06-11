import scala.swing._
import event._
import scala.swing.BorderPanel.Position._
import java.io.{File, PrintWriter}
import scala.io.Source
import java.awt.{Color}
import scala.collection.mutable.ListBuffer

class DiaryWindow extends MainFrame {
	/*
	TODO:
		- Searching entries:
			- Perhaps a little widget or something (not essential at this stage).

		- Settings menu:
			- Again, not essential at this stage but it's something I could look into.
	*/

	// Set some default window properties we want the application to adhere to.
	peer.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
	peer.setLocation(new Point(50, 50))

	// Initialise anything we may use in the window, we do this here as we need it to be accessible from all scopes within the class.
	var addEntryButton: Button = new Button
	var newEntryButton: Button = new Button
	var cancelEntryButton: Button = new Button

	var titleField: TextField = new TextField
	var entryField: TextArea = new TextArea

	var listScrollPane: ScrollPane = new ScrollPane
	var entryScrollPane: ScrollPane = new ScrollPane

	var label: Label = new Label

	var list: List[String] = List()
	var listBuffer: ListBuffer[String] = new ListBuffer()

	val listView: ListView[String] = new ListView(listBuffer)

	var listPanel: BorderPanel = new BorderPanel {}
	var entryPanel: BorderPanel = new BorderPanel {}
	var mainPanel: FlowPanel = new FlowPanel {}

	val applicationSize: Dimension = new Dimension(830, 650)

	val addEntryButtonText: String = "Add Entry"
	val addEntryButtonTextEditting: String = "Edit Entry"

	val cancelEntryButtonText: String = "Cancel Entry"
	val cancelEntryButtonTextEditting: String = "Delete Entry"

	val newEntryButtonText: String = "New Entry"
	val labelText: String = "Date             Entry"
	val folderTitle: String = "Diary Entries"
	val appTitle: String = "Simple Scala Diary"

	var isEditable: Boolean = false
	var currentEntry: Entry = new Entry()

	// Run the following on start up.
	Tools.setupEntryDirectory()
	init()
	applyTheme()
	loadListView()

	// Set the size and title of the app.
	preferredSize = applicationSize
	title = appTitle

	// Listen to the following components so we can receive updates about them.
	listenTo(addEntryButton, cancelEntryButton, newEntryButton, listView.selection)

	// Handle any possible reactions we may want in the app.
	reactions += {
		case ButtonClicked(b) => {
			if (b == addEntryButton && isEditable) editEntry(currentEntry, new Entry(titleField.text, entryField.text, currentEntry.date))
			if (b == addEntryButton && !isEditable) saveEntry(new Entry(titleField.text, entryField.text))
			if (b == cancelEntryButton && isEditable) deleteEntry(currentEntry)
			if (b == cancelEntryButton && !isEditable) clearEntrySpace()
			if (b == newEntryButton) {
				clearEntrySpace()
				addEntryButton.text = addEntryButtonText
				cancelEntryButton.text = cancelEntryButtonText
				isEditable = false
			}
		}

      		case SelectionChanged(`listView`) => {
      			if (listView.selection.items.length > 0) {
      				val entry: Entry =  Entry.loadEntry(listView.selection.items(0))
      				titleField.text = entry.title
      				entryField.text = entry.entry
      				currentEntry = entry
      			}
      			addEntryButton.text = addEntryButtonTextEditting
      			cancelEntryButton.text = cancelEntryButtonTextEditting
      			isEditable = true
      		}
	}

	// Define the sizes of elements and begin building the panels.
	label.preferredSize = new Dimension(400, 30)
	listScrollPane.preferredSize = new Dimension(400, 500)
	newEntryButton.preferredSize = new Dimension(400, 65)

	listPanel = new BorderPanel {
		layout(label) = North
		layout(new BorderPanel {
			layout(listScrollPane) = North
			layout(newEntryButton) = Center
		}) = Center
	}

	titleField.preferredSize = new Dimension(400, 30)
	entryScrollPane.preferredSize = new Dimension(400, 500)
	addEntryButton.preferredSize = new Dimension(200, 65)
	cancelEntryButton.preferredSize = new Dimension(200, 65)

	entryPanel = new BorderPanel {
		layout(titleField) = North
		layout(new BorderPanel {
			layout(entryScrollPane) = North
			layout(new FlowPanel { contents += addEntryButton; contents += cancelEntryButton; hGap = 0}) = Center
		}) = Center
	}

	// Finally, add the panels to our main windows contents.
	contents = new FlowPanel {
		contents += listPanel
		contents += entryPanel
	}

	/*
	Initialises all the variables etc.
	*/
	def init(): Unit = {
		addEntryButton.text = addEntryButtonText
		cancelEntryButton.text = cancelEntryButtonText
		newEntryButton.text = newEntryButtonText

		entryField.lineWrap = true
		entryField.wordWrap = true

		entryScrollPane.contents = entryField
		listScrollPane.contents = listView

		list = getEntries()

		label.text = labelText
		label.horizontalAlignment = Alignment.Left
		label.border = Swing.LineBorder(Color.BLACK, 1)
	}

	/*
		Applys colours to certain components.
	*/
	def applyTheme(): Unit = {
		newEntryButton.background = Color.decode("#2196F3")
		addEntryButton.background = Color.decode("#4CAF50")
		cancelEntryButton.background = Color.decode("#F44336")
		label.background = Color.decode("#FFFFFF")
		label.opaque = true
	}

	/*
		Writes the given entry file to a text document.
	*/
	def saveEntry(entry: Entry): Unit = {
		entry.save()
		clearEntrySpace()
      		loadListView()
	}

	/*
		Edits the given entry.
	*/
	def editEntry(oldEntry: Entry, newEntry: Entry): Unit = {
		deleteEntry(oldEntry)
		saveEntry(newEntry)
	}

	/*
		Deletes the given entry object.
	*/
	def deleteEntry(entry: Entry): Unit = {
		entry.delete()
		clearEntrySpace()
		loadListView()
	}

	/*
		This resets the field to what we want for new entries by default.
	*/
	def clearEntrySpace(): Unit = {
		titleField.text = ""
		entryField.text = ""
		titleField.requestFocus()
	}

	/*
		This reloads the listview everytime the data is updated.
	*/
	def loadListView(): Unit = {
		listBuffer.clear()
		listBuffer.appendAll(getEntries())
		listView.listData = listBuffer
	}

	/*
	This function gets a list of the text entries and strips off the extension.
	*/
	def getEntries(): List[String] = {
		var path: String = folderTitle + "/"

		var file: File = new File(path)
		var files: Array[File] = file.listFiles()

		var len: Integer = files.length
		var items: Array[String] = for(f <- files) yield f.getName.substring(0, f.getName.length - 4)


		for (i <- 0 until items.length) {
			var tup: (String, String) = stripEntry(items(i))
			items(i) = tup._1 + "    " + tup._2
		}

		items.toList
	}

	/*
	Takes an entry title and strips it into it's date part and it's title.
	*/
	def stripEntry(entry: String): (String, String) = {
		val SPLIT_POINT: Integer = 8

		var start: String = entry.substring(0, SPLIT_POINT)
		var end: String = entry.substring(SPLIT_POINT + 1)

		// Reverse our date as currently it is yy-MM-dd for sorting purposes.
		var split: Array[String] = start.split("-")
		var reversedDate: String = split.toList.reverse.mkString("-")

		(reversedDate, end)
	}
}