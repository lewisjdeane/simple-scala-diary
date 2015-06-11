import java.util.Date
import java.text.SimpleDateFormat
import scala.io.Source
import java.io.{PrintWriter, File, BufferedReader, FileReader}

object Entry {
	// Constant string which is the title of the folder in which we'll save entries in.
	val FOLDER_TITLE: String = "Diary Entries"

	/*
		Returns an entry object for the associated listitem.
	*/
	def loadEntry(_listItem: String): Entry = {
		val date: String = (_listItem.substring(0, 8).split("-")).toList.reverse.mkString("-")
		val title: String = _listItem.substring(12)
		val path: String = FOLDER_TITLE + "/" + date + "-" + title + ".txt"
		var entry: String = ""
		var br: BufferedReader = new BufferedReader(new FileReader(path))

		try {
			var sb: StringBuilder = new StringBuilder();
	        		var line: String = br.readLine();

	        		while (line != null) {
	            			sb.append(line);
	            			sb.append(System.lineSeparator());
	            			line = br.readLine();
	        		}
        			entry = sb.toString()
        			if (entry.length > 0)
        				entry = entry.substring(0, entry.length - 1)
		} finally br.close()

		val decryptedEntry: String = Security.decrypt(entry)

		new Entry(title, decryptedEntry, date)
	}

	/*
		Gets todays date in a formatted string.
	*/
	def getTodaysDate(): String = {
		var sdf: SimpleDateFormat = new SimpleDateFormat("yy-MM-dd")
		var now: Date = new Date()
		sdf.format(now)
	}
}

class Entry(_title: String, _entry: String, _date: String){
	// Constant string which is the title of the folder in which we'll save entries in.
	val FOLDER_TITLE: String = "Diary Entries"

	/*
		One of our other constructors, which uses todays date when one is not specified.
	*/
	def this(_title: String, _entry: String) {
		this(_title, _entry, Entry.getTodaysDate())
	}

	/*
		Used to create an empty object.
	*/
	def this() {
		this("", "")
	}

	/*
		Saves the current entry object to a plaintext file.
	*/
	def save(): Unit = {
		var path: String = FOLDER_TITLE + "/" + this._date + "-" + this._title + ".txt"

		val writer = new PrintWriter(new File(path))

		val encryptedEntry: String = Security.encrypt(this._entry)
	      	writer.write(encryptedEntry)
	      	writer.close()
	}

	/*
		Deletes the current object for the directory of entries.
	*/
	def delete(): Unit = {
		var path: String = FOLDER_TITLE + "/" + this._date + "-" + this._title + ".txt"
		new File(path).delete()
	}

	// Gets the title of this entry.
	def title = _title

	// Gets the entry text.
	def entry = _entry

	// Gets the date text.
	def date = _date
}