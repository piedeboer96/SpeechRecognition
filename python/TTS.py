import sys
import os
from gtts import gTTS
from pydub import AudioSegment

# Get the command-line arguments
text = sys.argv[1]  # The text to convert to speech
output_file_path = sys.argv[2]  # The output file path


language = 'en'
speech = gTTS(text=text, lang=language, slow=False)


mp3_path = output_file_path.replace('.wav', '.mp3')
speech.save(mp3_path)

# Convert mp3 to wav using pydub
sound = AudioSegment.from_mp3(mp3_path)
sound.export(output_file_path, format="wav")
#os.system("afplay " + output_file_path)
# Remove the temporary mp3 file
os.remove(mp3_path)
