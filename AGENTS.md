# Project AI Home Lab - shared conventions
This is Project AI Home Lab focused on use of local LLMs and PC hardware in local network for agentic programming.

## Hardware equipment
* AI-Server1 - primary AI server, ollama installed 
  CPU: AMD Ryzen 9 9950X 16-Core; Mobo: ASUS ProArt X870E-CREATOR; GPU: 2x Nvidia RTX PRO 4000 24G VRAM; RAM: 128GB DDR5; Disk: 2TB SSD;  
* AI-Server2 - secondary AI server 
  CPU: AMD Ryzen 7 5700G 8-Core; Mobo: ASUS PRIME B550M-A; GPU: no GPU yet considering AMD Radeon AI PRO R9700 32 GB; RAM: 128GB DDR4; Disk: 2TB SSD;  
* Work-PC - Lenovo Pro 5 16AFR10 laptop
  CPU: AMD Ryzen 9 9955HX 16-Core; GPU: NVIDIA GeForce RTX 5070 8G; RAM: 64GB DDR5; Disk: 4TB SSD
* Home-Controller - low power PC to deploy and run local microservices
  CPU: Intel Pentium Gold G6405 4-Core; GPU: intel integrated; RAM: 32GB DDR4; Disk: 1T SSD   
* All PCs are on same local 2.5 GB/s network
* PCs are running Ubuntu 26.04 LTS OS, Home-Controller is running Ubuntu 24.04.4 LTS OS

## Targets 
* Create home local agentic coding system (using local LLMs), which will accept requirements in form of README.md or prompt and create Spring Boot 4 / Java 25 / gradle microservices and deploy resulting docker images on Home-Controller machine.
* Work-PC is used to run (orchestrate) agentic coding, compile code, run unit tests and smaller local LLMs for validation, AI-Server1 is supposed to do heavy LLM lifting, AI-Server2 is supposed to help AI-Server1 running smaller local LLMs.
* Documentation and source code is stored on Work-PC in repos directory.
* Home-Controller is supposed to run resulting deployed docker microservices. 
* Priority is not speed (tokens/s) but resulting code quality. Slightly oversized LLMs are acceptable. Prompt processing for ~20 minutes is acceptable.
* Whole system should run automatically: will tage task description and iterate on coding / testing / deployment untill the task is done.
* Only local LLMs are used running on Work PC, AI Server1 and AI Server2 
* Deployment on Home-Controller as docker-compose stack. 
* Me as programmer, I want to give agent a prompt, wait for implementation, test result and deployment to Home-Controller PC. When done, I want to review the resultsing changes in git repositories.

## Non-negotiable
- Only local agents
- Only local LLMs: https://ollama.com/, https://huggingface.co/ or others if suitable
- Spring Boot 4, not 3. `spring.factories`, or javax.* imports. If unsure, check an existing
  service before inventing config.
- Java 25, Gradle 9
- All microservices will expose REST APIs.
- Integration tests use Testcontainers, no shared test DB.
- UI is a web page, some simple framework like react.

## Git
- generated code is stored in git repositories in `repos` directory 
- `main` branch is always stable and functional 
- no git submodules

