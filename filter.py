# -*- coding: utf-8 -*-
import re
import configparser
from imgurpython import ImgurClient
import sys

arg_int = int(sys.argv[1])

print 'logging into imgur'

config = configparser.ConfigParser()
config.read('auth.ini')

client_id = config.get('credentials', 'client_id')
client_secret = config.get('credentials', 'client_secret')

client = ImgurClient(client_id, client_secret)

li = []
filtered_li = []
new_album = False

print 'filtering...'

with open('new.txt') as f:
    new_links = f.readlines()
new_li = list([x.strip() for x in new_links])
if len(new_li) > 0:
    new_album = True

for link in range(len(new_li)):
    if 'imgur.com/a/' in  new_li[link]:
        albm_id = re.findall(r'/a/(\w+)/?\s*', new_li[link])
        li.append(albm_id)

count = 0   
for imgr_id in range(len(li)):
    try:
        if len(client.get_album_images(li[imgr_id][0])) >= arg_int:
            filtered_li.append('https://imgur.com/a/' + str(li[imgr_id][0]))
            count += 1
    except:
        continue

filtered_out_li = [x for x in new_li if x not in set(filtered_li)]
        
with open("filtered.txt", "w") as filter_file, open("filtered-out.txt", "w") as filtered_out_file:
    filter_file.write("\n".join(filtered_li))
    filtered_out_file.write("\n".join(filtered_out_li))
    
print 'process 2 complete'