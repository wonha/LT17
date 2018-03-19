__Part Of Speech Tagging with Hidden Markov Models__<br/>
[GitPitch]()<br />
wonhashin26@gmail.com

---
@title[Agenda]

- NLP Overview
- Formal Model and Language
- Grammer
- Morphological Analysis - word segmentation
- Morphological Analysis - POS tagging

---
@title[NLP Overview]

+++
- Natural Language Processing
    1. Natural Language Understanding/Analysis
        1. keywork detection
        1. understanding syntactic structure or sematic interpretation of sentences
    1. Natural Language Generation
        1. translation
        1. response of system (dialogue system) 

+++

- Natural Language Understanding/Analysis
    1. Mophological analysis
        1. word segmentation
        1. part of speech
        1. conjugation (plural form of nouns, conjugation of verbs)
    1. Syntactic analysis
        1. phrase, clause
        1. head modifier relation (Generation of tree structure)
    1. Semantic analysis
        1. Determining semantic types of head modifier relation
        1. Determining meanings of words
    1. Discourse analysis
        1. Discourse structure analysis

+++
Required Knowledge for Morphological Analysis

1. Lexicon (Dictionary)
    1. Word : POS
        - e.g, 'desk : noun'
    1. Conjugation type
1. Rules to produce conjugation form
1. Rules to order POS

+++
Required Knowledge for Syntactic Analysis

1. Grammer - tree (today's topic)
    - Context Free Grammer - derivation tree
        - Phrase Structured Grammer (CFG describing structures of NL) - parse tree
    - Chomsky Normal Form
        - Any CFG can be converted to CNF
    - Multiple parse tree : multiple interpretations of given S

+++
Required Knowledge for Syntactic Analysis

1. Case frame (e.g, IPAL)
    - Case : semantic relation between words
1. Association

+++

- Natural Language Generation
    1. Fixed Sentence
        - telephone operator, ATM ...
    1. Generation using template
        - 'Current temperature is ${temperature}'
    1. Generation using semantic interpretation 

---
@title[Formal Model and Language]

+++

- Formal model and Languages
    - : Given S -> model G - > L(G)

- Formal Language : Set of strings
- Formal Model : Definition of language 
- Fromal Grammer : Set of production rules

+++

Formal Models(Chompsky Hierarchy)
  - : Given S -> model G - > L(G)

1. Type 3 : Finite Automaton (Regular Grammer) -> RegularLanguage(RG)
    - Can't accept NL
        - e.g, (that)i it is false (is false)i where i->infinite
1. Type 2 : Pushdown Automaton (Context Free Grammer) -> ContextFreeLanguage(CFG)
    - Efficiently accepts Natural Language
        - e.g, Phrase Structured Grammer
1. Type 1 : Linear bounded Automaton (Context Sensitive Grammer) -> ContextSensitiveLanguage(CSG)
1. Type 0 : Turing Machine (Type 0 Grammer) -> Type0Language(T0G)

+++

- Formal model and Languages
    - : Given S -> model G - > L(G)
- Differnece between formal model and language model ?
    - [Language Model (refer p.16)](http://www.phontron.com/slides/nlp-programming-en-01-unigramlm.pdf)


---
@title[Grammer(Syntactic Analysis)]

- Parse Tree
    Non-terminal symbol
    Terminal symbol : word (derived by _Lexical rule_)
    Pre-terminal symbol : POS (determined by _pharse structured rule_)

+++
Phrase Structured Rules
S -> NP VP
VP -> v

Lexical Rules
v -> eat
pron -> I

_More practical grammers like Unification Grammer (grammer using feature structure) or HPSG (no production rule) are used in real world_

+++

Parsing Algorithms :
  - Top-down algorithm
  - Bottom-up algorithm
    CKY(O(n3)), Chart parsing(O(n3)), Recurisve transition network...

---
@title[Morphological Analysis - word segmentation]

Morpheme : minumum linguistic unit
    - word : stem + affix (e.g, play-ing)

+++

Word Dictionary cantains...
    - POS
    - Pronunciation
    - Sense
    - Case frame
        - Only POS is required for morphological analysis


---
@title[Morphological Analysis - POS tagging]

Sometimes it is better to give Sequence of POS, rather than just give S to reduce computation effort