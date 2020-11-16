# Aether Commons
This module contains all classes and functions which are required by all other modules in AetherElements, 
like domain model and database.

## Database
I decided to use a very simple model with json, csv and text files, in order that you can add more modules of your own.
So you can write your modules in any language, like for example python, and it still works with the same "database".

Data is stored inside a folder "database".
There are subfolders for cases, hotbits, images and layers, queue, rates and setting. 

## HotbitsHandler and AnalysisService
You need two classes for a simple radionics procedure. The **HotbitsHandler** "handles" the hotbits, it loads them from the harddisk
(or database) and serves them to the **AnalysisService**, integer by integer. If there are no hotbits on the harddisk,
it switches over to a **SecureRandom** object, which in some case may deliver good enough result (but not perfect). This
depends on the operating system and the hardware. Some computer systems have a random generator build in 
(example Raspberry Pi). The SecureRandom object tries to maximize the entropy value of the random numbers for 
ecryption, which is also good enough for radionics. Instead the pseudo random generator in Java called just Random is
only useful for simulations and teaching purposes.

    private Random getRandom() {

        try {

            loadHotbitsFromHarddisk();

            if (hotbits.size() > 0) {
                simulationMode = false;
                return new Random(hotbits.remove(0));
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        simulationMode = true;
        return secureRandom;
    }

Naturally what you will see is that the Hotbit integer is used as a seed for the conventional Random object as a
convinience for getting the different range of numbers or boolean values.

    public Integer nextInteger(int min, int max) {
        return getRandom().nextInt(max-min) + min;
    }

