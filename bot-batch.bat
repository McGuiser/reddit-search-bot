echo off
set string=%1
set int=%2
@C:\Users\User\python.exe X:\User\Documents\reddit-search-bot\bot.py %string%
@C:\Users\User\python.exe X:\User\Documents\reddit-search-bot\filter.py %int%
echo on
