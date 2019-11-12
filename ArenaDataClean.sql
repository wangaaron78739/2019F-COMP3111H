DROP DATABASE IF EXISTS ArenaData;
CREATE DATABASE ArenaData;
USE ArenaData;

CREATE TABLE IF NOT EXISTS Tower (
   id             INT unsigned NOT NULL AUTO_INCREMENT, # Tower Num
   attack_power   INT default NULL,                # Attack Power
   building_cost  INT default NULL,                # Building
   shooting_range INT default NULL,                # Shooting Range
   x              INT default NULL,                # X Coordinate
   y              INT default NULL,                # Y Coordinate
   type           ENUM('Basic','Catapult','Ice','Laser'),        # Type of Tower
   upgrade_cost   INT default NULL,                # Upgrade Cost
   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Monster (
   id             INT unsigned NOT NULL AUTO_INCREMENT, # Monster Num
   xpx            FLOAT default NULL,                # x position in Px
   ypx            FLOAT default NULL,                # y position in Px
   hp             INT default NULL,                # Hit Points
   speed          INT default NULL,                # Speed
   type           ENUM('Fox','Unicorn','Penguin'),        # Type of Monster
   direction      ENUM('UP','Down','Left','Right'), #Direction of Monster
   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS GameData (
   id             INT unsigned NOT NULL AUTO_INCREMENT, # Monster Num
   frame_count    INT default NULL,                # Hit Points
   resource_amt   INT default NULL,                # Resource Amount
   game_state     ENUM('not_started', 'play', 'simulate', 'ended'), # Game State
   PRIMARY KEY (id)
);