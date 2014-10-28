class CreateDevices < ActiveRecord::Migration
  def change
    create_table :devices do |t|
      t.string :type
      t.string :name
      t.string :category
      t.string :devicenum
      t.string :net
      t.string :system
      t.string :screen_resolution
      t.string :nums
      t.string :ownner
      t.string :serisies_num    
      t.string :status
      t.string :rent_by
    end
  end
end
