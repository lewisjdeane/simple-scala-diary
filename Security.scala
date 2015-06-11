import swing._
import swing.BorderPanel.Position._
import event._
import scala.math._

object Security {
	// Gets the users inputted password which is used to encrypt/decrypt the entries.
	def KEY: String = Diary.PASS

	/*
		Returns an encryption of the given plaintext and outputs it in hex form.
	*/
	def encrypt(plaintext: String): String = {
		val keyBits: Array[Int] = toBitArray(KEY.toCharArray)
		val messageBits: Array[Int] = toBitArray(plaintext.toCharArray)

		val len: Int = messageBits.length

		var outputBits: Array[Int] = new Array[Int](len)

		for (i <- 0 until len) outputBits(i) = messageBits(i) ^ keyBits(i % keyBits.length)

		convertToHex(outputBits)
	}

	/*
		Takes a hex encryption and outputs the plaintext.
	*/
	def decrypt(cipherText: String): String = {
		var keyBits: Array[Int] = toBitArray(KEY.toCharArray)
		var encryptedBits: Array[Int] = fromHexToBits(cipherText)
		val len: Int = encryptedBits.length
		var outputBits: Array[Int] = new Array[Int](len)

		for (i <- 0 until len) outputBits(i) = encryptedBits(i) ^ keyBits(i % keyBits.length)

		convertToPrintable(outputBits)
	}

	/*
		Converts a list of bits to hex.
	*/
	private def convertToHex(bits: Array[Int]): String = {
		val len: Int = bits.length
		var dec: Array[Int] = new Array[Int](len / 8)

		for (i <- 0 until len / 8) {
			var num: Int = 0
			for (j <- 0 until 8)
				num += Math.pow(2, 7 - j).toInt * bits(i * 8 + j)
			dec(i) = num.toInt
		}

		var str: String = ""

		for (i <- 0 until len / 8) {
			var a: Int = floor(dec(i) / 16).toInt
			var b: Int = dec(i) - (a * 16)

			str += getHexValue(a) + getHexValue(b)
		}

		str
	}

	/*
		Returns the base16 equivalent of a decimal number.
	*/
	private def getHexValue(num: Int): String = num match {
		case 10 => "A"
		case 11 => "B"
		case 12 => "C"
		case 13 => "D"
		case 14 => "E"
		case 15 => "F"
		case _ => num.toString
	}

	/*
		Opposite to above, gets the decimal value of a character from its hex representation.
	*/
	private def getValFromHex(ch: Char): Integer = ch match {
		case 'A' => 10
		case 'B' => 11
		case 'C' => 12
		case 'D' => 13
		case 'E' => 14
		case 'F' => 15
		case _ => ch.toInt - 48
	}

	/*
		Converts a string of hex to an array of bits.
	*/
	private def fromHexToBits(hex: String): Array[Int] = {
		val len: Int = hex.length / 2
		var dec: Array[Int] = new Array[Int](len)

		for (i <- 0 until len) {
			var num = 0
			for (j <- 0 until 2) num += getValFromHex(hex.charAt(i * 2 + j)) * Math.pow(16, 1 - j).toInt
			dec(i) = num
		}

		var bits: Array[Int] = new Array[Int](len * 8)

		for (i <- 0 until len) {
			var bin: Array[Int] = decToBinary(dec(i))
			for (j <- 0 until 8) bits(i * 8 + j) = bin(j)
		}

		bits
	}

	/*
		Converts a decimal number to an array representing its base 2 notation.
	*/
	private def decToBinary(num: Int): Array[Int] = {
		var bin: Array[Int] = new Array[Int](8)
		var n: Int = num

		for (i <- 0 until 8) {
			bin(7 - i) = n % 2
			n = n / 2
		}

		bin
	}

	/*
		Converts the list of bits to a printable form.
	*/
	private def convertToPrintable(bits: Array[Int]): String = {
		var str: String = ""

		for (i <- 0 until bits.length / 8) {
			var bitVal: Int = 0
			for (j <- 0 until 8) bitVal = bitVal + (Math.pow(2, 7 - j) * bits((8 * i) + j)).toInt
			str = str + PrintableAscii.getChar(bitVal)
		}

		str
	}

	/*
		Converts an array of character to an array of bits.
	*/
	private def toBitArray(charArray: Array[Char]): Array[Int] = {
		val len: Int = charArray.length
		var bitArray: Array[Int] = new Array[Int](len * 8)

		for (i <- 0 until len) {
			var bits: Array[Int] = charToBits(charArray(i))
			for (j <- 0 until 8) bitArray((i * 8) + j) = bits(j)
		}

		bitArray
	}

	/*
		Converts a char to a bit array representing its binary form.
	*/
	private def charToBits(char: Char): Array[Int] = {
		var charVal: Int = PrintableAscii.getVal(char)
		var bitArray: Array[Int] = new Array[Int](8)

		for (i <- 0 until 8) {
			bitArray(7 - i) = charVal % 2;
			charVal = charVal / 2;
		}

		bitArray
	}

	private object PrintableAscii {
		def getVal(char: Char): Int = if (char.toInt > 31 && char.toInt < 127) char.toInt - 32 else 0

		def getChar(int: Int): Char = (int + 32).toChar
	}
}