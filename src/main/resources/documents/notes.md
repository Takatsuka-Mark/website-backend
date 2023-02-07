# General Notes

## How functions are defined

#### Funciton Definition
| param_name | description |
| --- | --- |
| title | The name of funciton |
| description | The description of what the function does |
| pattern | The regex pattern of the funciton to match (eg: ) |
| MathMethod | The information about the method to call |
| ParamPattern | A regex pattern describing the allowed paramter configurations |
| param_description | A description of the allowed paramter patterns |
| FUNCTION | the function ?? Is this necessary? |
| is_in_place | True if the function is an in-place method |

#### ParamTypes
| param_pattern_value | description |
| --- | --- |
| N | natural numbers [0, inf] |
| Z | Integers |
| Q | Rationals |
| fun | The function itself - useful for in-place operations |
| R | Reals |
