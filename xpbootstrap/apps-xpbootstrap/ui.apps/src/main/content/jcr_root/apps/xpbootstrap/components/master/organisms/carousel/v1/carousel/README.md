
Carousal Organism (v1)
====
Carousal organism written in HTL. Carousel is a container component

## Features

* Allows addition of Carousel item components of varying resource type.
* Allowed components can be configured through policy configuration.
* Carousel navigation via next/previous and position indicators.
* Carousel autoplay with: 
  * Configurable delay.
  * Ability to disable automatic pause on hover.
  * Pause/play buttons.
  * Automatic pausing when the document is hidden, making use of the [Page Visibility API](https://developer.mozilla.org/en-US/docs/Web/API/Page_Visibility_API).
* Editing features for items (adding, removing, editing, re-ordering).
* Allows deep linking into a specific panel by passing the panel id as the URL fragment

### Use Object
The Banner molecule uses the `com.xperience.aem.xpbootstrap.core.models.organisms.Carousel` Sling model as its Use-object.

### Component Policy Configuration Properties
The following configuration properties are used:

1. `./autoplay` - defines whether or not the carousel should automatically transition between slides.
2. `./delay` - defines the delay (in milliseconds) when automatically transitioning between slides.
3. `./autopauseDisabled` - defines whether or not automatic pause when hovering the carousel is disabled.
4. `./controlsPrepended` - defines whether the carousel controls should be arranged before the carousel items or not.

### Edit Dialog Properties
The following properties are written to JCR for this Accordion component and are expected to be available as `Resource` properties:

1. `./autoplay` - defines whether or not the carousel should automatically transition between slides.
2. `./delay` - defines the delay (in milliseconds) when automatically transitioning between slides.
3. `./autopauseDisabled` - defines whether or not automatic pause when hovering the carousel is disabled.
4. `./controlsPrepended` - defines whether the carousel controls should be arranged before the carousel items or not.

Note: on author instances automatic transitioning only works with the `wcmmode=disabled` URL parameter.

## Client Libraries
It uses bootstraps breadcrumb component CSS [https://getbootstrap.com/docs/5.0/components/carousel/]
It should be added to a relevant site client library using the `embed` property.


## Information
* **Vendor**: Deloitte
* **Version**: v1
* **Compatibility**: AEM 6.5
* **Status**: production-ready
* **Documentation**: [http://localhost:4502/content/adlc-component-library/us/en/home/carousel-organism.html](http://localhost:4502/content/adlc-component-library/us/en/home/carousel-organism.html)
* **Component Library**: [http://localhost:4502/content/adlc-component-library/us/en/home/carousel-organism.html](http://localhost:4502/content/adlc-component-library/us/en/home/carousel-organism.html)
* **Author**: [Deloitte]()
* **Co-authors**: [Deloitte](), [BhushanBashire](https://github.com/BhushanBashire)

