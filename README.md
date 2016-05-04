# Decanter
Your cellar data, direct from the Ark

## Installation

TODO

## Usage

You will need your wine-xchange credentials:

* Client number
* Password

### Synchronization

The following command will extract the latest cellar inventory and update the
locally configured database.

```
decanter sync --client-number=12345 --password=MyPassword
```

### Querying

An example. To display all wine produced by Lake's Folly, of a vintage older than 2010, use the following:

```
decanter query --producer="Lake's Folly" --vintage="before 2010"
```
