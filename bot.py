# -*- coding: utf-8 -*-
import praw
import re
import sys

reddit = praw.Reddit('bot1', user_agent='bot1 user agent')

arg_str = str(sys.argv[1])
arg_li = [arg for arg in arg_str.split(',') if arg != 'null']
key_li = arg_li[4:]
print arg_str
print arg_li
print key_li
print arg_li[3].split()[0].lower()

arg_limit = float(arg_li[0])
sub_counter = 0.0

if arg_li[3] == 'Hot':
    if arg_li[1] == 'Front Page':
        search_in = reddit.front.hot(limit=int(arg_li[0]))
    else:
        search_in = reddit.subreddit(arg_li[1]).hot(limit=int(arg_li[0]))
else:
    if arg_li[1] == 'Front Page':
        print arg_li[3].split()[0].lower()
        search_in = reddit.front.top(arg_li[3].split()[0].lower(), limit=int(arg_li[0]))
    else:
        search_in = reddit.subreddit(arg_li[1]).top(arg_li[3].split()[0].lower(), limit=int(arg_li[0]))


print 'Logging into reddit'

archive_li = []
new_li = []
new_album = False
            
with open('links.txt') as f:
    archived_links = f.readlines()
archive_li = list([x.strip() for x in archived_links])

print 'Searching...'

for submission in search_in:
    found = False
    if not submission.stickied:
        for keyword in key_li:
            if keyword in submission.url:
                found = True
                break
        if found:
            if not submission.url in archive_li:
                new_li.append(submission.url)
                archive_li.append(submission.url)
        submission.comments.replace_more(limit=0)
        for comment in submission.comments.list():
            found = False
            for keyword in key_li:
                if keyword in comment.body:
                    found = True
                    break
            if found:
                comment_regex = re.findall(r'(https?://[^\s]+)', comment.body)
                for url_found in comment_regex:
                    if '](' in url_found:
                        url_found = url_found.split('](')[1]
                    if ')' in url_found:
                        url_found = '(' + url_found
                    else:
                        url_found = '(' + url_found + ')'
                    url_regex = re.findall(r'\((https?://[^\s]+)\)', url_found)
                    for keyword in key_li:
                        if keyword in url_regex[0]:
                            if not url_regex[0] in archive_li:
                                new_li.extend(url_regex)
                                archive_li.extend(url_regex)
                                break
    sub_counter = sub_counter + 1.0
    percent_done = (sub_counter / arg_limit) * 100.0
    print percent_done, '%'
    sys.stdout.flush()
                
print 'done searching.'

archive_li = list(set(archive_li))
new_li = list(set(new_li))

with open("links.txt", "w") as archive_file, open("new.txt", "w") as new_file:
    archive_file.write("\n".join(archive_li))
    new_file.write("\n".join(new_li))
    
print 'proccess 1 complete'