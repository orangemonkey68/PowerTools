# Submitting a Bug Report

This will be quick, I promise!

----

## Before you start

- Check if your bug has already been reported
    - This saves everyone time, but most importantly, it saves yours! Don't write a whole bug report if someone already
    has for you!
  - If it's already been reported, add whatever information you can to the situation!
- Try to reproduce the bug
    - It's pretty hard to fix a bug that I can't find! Try to collect as much information (and logs) as possible.

## Writing the Report

- ANY information on the system is incredibly helpful. Key info could include
    - Operating System and Version
    - Other Mods (and their versions) that are installed
    - Description of Bug/Behavior
    - Any FULL AND COMPLETE logs from that game session
    - If it's an API bug / a conflict with a mod you're coding, please provide the code you're using
    - Most importantly: Steps to reproduce
    
## Example / Template

- Operating System: Windows 10, Version 20H2, Build 19042.1110 
- Mods: Iris v1.1.1, Sodium v0.3.0, PowerTools 0.1.0
- Description: When right-clicking the ground with a PowerTool that is bound to an item, the item disappears from the client's
inventory. The item will move to the cursor slot when clicked or updated, but it appears missing in the client inventory, and no other data is lost.
- Logs: Link(s) to Hastebin.com
- Steps to reproduce:
    1. Get a block of sand, in survival mode
    2. Perform the command "/powertools create "say hi""
    3. Attempt to place the block on the ground, then notice it's missing
    4. Open the inventory and click the slot that the sand was in, or re-log to get it to re-appear.