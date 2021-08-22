# PowerTools
Bind commands to ANY item

## Setup
Simply drop the `powertools-common` and `powertools-PLATFORM` jars into the platform of your choice!


## FAQ

- Q: Why isn't there a version for [insert version here]?
    - A: There will be soon. Check [the roadmap section](#roadmap) to see where in line your platform is!
- Q: I found a bug, what should I do?
    - A: Please read [Submitting A Bug Report](https://github.com/orangemonkey68/PowerTools/blob/master/SubmittingABugReport.md), and follow the steps found there.

## Roadmap

- [x] Write API Module
- [x] Update buildscript publishing
- [ ] Write Fabric Module
    - [ ] Publish to Github
    - [ ] Publish to Modrinth
    - [ ] Publish to CurseForge <sup>[1](#footnote-1)</sup>
- [ ] Write Forge Module
- [ ] Write Spigot/Paper Module
- [ ] Implement client-sided features for Fabric and Forge Modules
- [ ] Write Sponge Module<sup>[2]($footnote-2)</sup>

#### Footnotes

<a name="footnote-1">1</a>: Updated when approved on CurseForge  
<a name="footnote-2">2</a>: This one is still a big "maybe", since I know very little about sponge, and dnon't expect a large demand for it.

## Commands

| Name | Syntax | Permission Node | Aliases | 
| ---- | ------ | --------------- | ------- |
| PowerTools | `/powertools` | TBD | `/pt` |
| Info | `/powertools info` | TBD | `/pt info`, `/pt i` |
| Create | `/powertools create` | TBD | `/pt create`, `/pt make` |
| Delete | `/powertools delete` | TBD | `/pt delete`, `/pt del` |
| Add User | `/powertools adduser` | TBD | `pt auth`, `pt authuser` |
| Del User | `powertools deluser` | TBD | `pt de-auth`, `pt de-authuser`|

## License

This mod is under the GPL3.0 license. Feel free to learn from it and contribute!
