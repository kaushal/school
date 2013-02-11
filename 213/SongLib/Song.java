public class Song {
	//This class will contain the song objects that the gui will display. 
	//The "list" of songs will be a linked list of song objects
	String name, artist, year, album;
	public Song(String name, String artist, String year, String album){
		this.name = name;
		this.artist = artist;
		this.year = year;
		this.album = album;
	}
	
	public String toString(){
		return name;
	}
}
