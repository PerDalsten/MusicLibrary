select * from MUSICLIBRARY.ARTIST where lower(ARTIST_NAME) like '%sat%';
select * from MUSICLIBRARY.ARTIST where id=83;

select * from MUSICLIBRARY.SONG where album_id=416;

select * from MUSICLIBRARY.ALBUM where id = 600;

select distinct(album_artist) name from MUSICLIBRARY.ALBUM order by name asc;