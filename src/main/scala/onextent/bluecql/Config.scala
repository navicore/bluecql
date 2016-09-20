package onextent.bluecql

import java.io.File

trait Config {

  def fieldName(name: String): String = {
    Character.toLowerCase(name.charAt(0)) + name.substring(1)
  }
  def caseName(name: String): String = {
    Character.toUpperCase(name.charAt(0)) + name.substring(1) + "Data"
  }

  def pdir(): String = {
    val regex = "\\.".r
    val pkgpath = regex.replaceAllIn(property(PACKAGE_PROP), "/")
    val pkgdir = s"${property(OUT_DIR_PROP)}/src/main/scala/${pkgpath}"
    new File(s"${pkgdir}").mkdirs()
    pkgdir
  }

  // keys
  def PACKAGE_PROP: String = "PACKAGE"
  def FILE_PROP: String = "FILE"
  def OUT_DIR_PROP: String = "OUT"

  // set / get
  def property(name: String, value: String): Unit = System.setProperty(name, value)
  def property(name: String): String = System.getProperty(name)

}

