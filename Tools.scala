import java.io.{File, BufferedReader, FileReader}
import java.security.{MessageDigest}

object Tools {
	// Constant string which is the title of the folder in which we'll save entries in.
	val FOLDER_TITLE = "Diary Entries"

	/*
		Returns a hash of the password.
	*/
	def hash(pass: String): String = {
		var digest: MessageDigest = MessageDigest.getInstance("SHA-256")
		var hash: Array[Byte]  = digest.digest(pass.getBytes("UTF-8"))

		var sb: StringBuilder = new StringBuilder()

    		for (b <- hash) 
        			sb.append("%02X ".format(b))

        		sb.toString
	}

	/*
		Reads the hash from the save file.
	*/
	def getHashedPassword(): String = {
		val path: String = "PASSWORD.txt"
		var br: BufferedReader = new BufferedReader(new FileReader(path))

		try {
	        		br.readLine()
		} finally br.close()
	}

	/*
		Boolean to determine whether or not there exists a password save file.
	*/
	def passwordExists: Boolean = {
		val file: File = new File(".")
		val files: Array[File] = file.listFiles()

		var fileExists: Boolean = false

		for (f <- files) { if(f.getName() == "PASSWORD.txt") fileExists = true  }

		fileExists
	}

	/*
		Sets up the directory where all entries will be saved.
	*/
	def setupEntryDirectory(): Unit = {
		if (directoryExists) return

		var dir: File = new File(FOLDER_TITLE)
		dir.mkdir()
	}

	def directoryExists: Boolean = {
		var file: File = new File(".")
		var files: Array[File] = file.listFiles()

		for (f <- files) {
			if (f.getName() == FOLDER_TITLE) true
		}

		false
	}

	/*
		Wipes the directory when a new password is set up.
	*/
	def clearEntryDirectory(): Unit = if (directoryExists) {
		var file: File = new File(FOLDER_TITLE)

		if (file.list().length == 0) file.delete()
    		else {
        			var files: Array[String] = file.list()
 
			for (temp <- files) {
				new File(file, temp).delete()
			}

			if (file.list().length == 0) file.delete()
		}
	}
}