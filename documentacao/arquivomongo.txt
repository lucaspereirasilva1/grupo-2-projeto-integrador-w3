show dbs

use frescos

db.createCollection("section");
db.createCollection("warehouse");

show collections

db.warehouse.insertMany([
    {
        "warehouseCode" : "SP",
        "warehouseName" : "sao paulo"
    },
    {
        "warehouseCode" : "MG",
        "warehouseName" : "minas gerais"
    },]
    )

db.section.insertOne(
    {
        "sectionCode": "LA",
        "sectionName": "laticinios",
        "maxLength": 1,
        "warehouse" : ["SP", "MG"]
    }
)

db.section.find()
db.warehouse.find()

db.section.aggregate([{
    $lookup: {
        from: "warehouse",
        localField: "warehouse",
        foreignField: "warehouseCode",
        as: "warehouses"
    }
}]).pretty()

db.section.updateOne(
    {
        "sectionCode" : "LA"
    },
    {$set:{
        "warehouse": [
            {
                "_id" : ObjectId("616a29610d5fba319e233f9c"),
                "warehouseCode" : "SP",
                "warehouseName" : "sao paulo"
            }
        ]
    }
    }
    )

db.section.deleteOne(
    {
        "_id" : ObjectId("616a3d860d5fba319e233fa5")
    }
    )